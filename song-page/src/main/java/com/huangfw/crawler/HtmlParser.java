package com.huangfw.crawler;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URL;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import com.huangfw.crawler.model.MusicCommentMessage;
import com.huangfw.crawler.model.Song;
import com.huangfw.crawler.model.WebPage;
import com.huangfw.crawler.repository.SongRepository;
import com.huangfw.crawler.utils.Constants;
import com.huangfw.crawler.model.MusicComment;
import com.huangfw.crawler.utils.EncryptUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.jsoup.Connection;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONPath;
import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;

public class HtmlParser {
    @Autowired
    private SongRepository songRepository;

    private static final HtmlFetcher HTML_FETCHER = new HtmlFetcher();
    private static final String BASE_URL = "http://music.163.com/";
    private static final String text = "{\"username\": \"\", \"rememberLogin\": \"true\", \"password\": \"\"}";
    
    public List<WebPage> parsePlaylists(String url) {
        Document document = Jsoup.parse(HTML_FETCHER.fetch(url));
        Elements playlists = document.select(".tit.f-thide.s-fc0");
        return playlists.stream().map(e -> new WebPage(BASE_URL + e.attr("href"), WebPage.PageType.playlist)).collect(Collectors.toList());
    }
    
    public List<WebPage> parsePlaylist(String url) {
        Elements songs = Jsoup.parse(HTML_FETCHER.fetch(url)).select("ul.f-hide li a");
        Long commentCount = Long.valueOf(0);
        Long fav = Long.valueOf(0);
        Long share = Long.valueOf(0);
        Long commentCountList = Long.valueOf(0);
        Long playCount = Long.valueOf(0);
        String tag = new String();
        try {
            Elements favs = Jsoup.parse(HTML_FETCHER.fetch(url)).select("a.u-btni.u-btni-fav i");
            for(Element e : favs){
                String favStr = e.html().replaceAll("万","0000");
                int endIndex = favStr.length()-1;
                fav = Long.valueOf(favStr.substring(1,endIndex)).longValue();
            }
            Elements shares = Jsoup.parse(HTML_FETCHER.fetch(url)).select("a.u-btni.u-btni-share i");
            for(Element e : shares){
                String shareStr = e.html().replaceAll("万","0000");
                int endIndex = shareStr.length()-1;
                share = Long.valueOf(shareStr.substring(1,endIndex)).longValue();
            }
            Elements comments = Jsoup.parse(HTML_FETCHER.fetch(url)).select("a.u-btni.u-btni-cmmt i span");
            for(Element e : comments){
                commentCountList = Long.valueOf(e.html().replaceAll("万","0000")).longValue();
            }
            Elements tags = Jsoup.parse(HTML_FETCHER.fetch(url)).select("a.u-tag i");
            if(tags.size()>=1){
                tag = StringEscapeUtils.escapeHtml4(tags.get(0).html());
            }
            Elements playCounts = Jsoup.parse(HTML_FETCHER.fetch(url)).select("Strong#play-count.s-fc6");
            playCount = Long.valueOf(playCounts.get(0).html()).longValue();
        } catch (Exception e) {
            e.printStackTrace();
        }

        /*for(Element e : songs){
            //System.out.println(BASE_URL + e.attr("href")+" "+e.html()+" "+fav+" "+share+" "+commentCountList+" "+playCount+" "+tag);
            Song song = new Song(BASE_URL + e.attr("href"),e.html(),commentCount,fav,share,commentCountList,playCount,0,tag);
            System.out.println(song);

            try {
                songRepository.saveAndFlush(song);
                System.out.println("保存成功");
            } catch (Exception e1) {
                e1.printStackTrace();
                System.out.println("保存失败");
            }
        }*/
        List<WebPage> webPages = new ArrayList<WebPage>();
        for(Element e: songs){
            WebPage webPage = new WebPage(BASE_URL + e.attr("href"), WebPage.PageType.song, e.html(), fav,share,commentCountList,playCount,tag);
            webPages.add(webPage);
        }
        return webPages;
        //return songs.stream().map(e -> new WebPage(BASE_URL + e.attr("href"), WebPage.PageType.song, e.html(), fav,share,commentCountList,playCount,tag)).collect(Collectors.toList());
    }
    
    public Long parseSong(String url) {
        try {
            return getCommentCount(url.split("=")[1]);
        } catch (Exception e) {
            return 0L;
        }
    }

    private Long getCommentCount(String id) throws Exception {
        String secKey = new BigInteger(100, new SecureRandom()).toString(32).substring(0, 16);
        String encText = aesEncrypt(aesEncrypt(text, "0CoJUm6Qyw8W8jud"), secKey);
        String encSecKey = rsaEncrypt(secKey);
        Response response = Jsoup
                .connect("http://music.163.com/weapi/v1/resource/comments/R_SO_4_" + id + "/?csrf_token=")
                .method(Connection.Method.POST).header("Referer", BASE_URL)
                .data(ImmutableMap.of("params", encText, "encSecKey", encSecKey)).execute();
        return Long.parseLong(JSONPath.eval(JSON.parse(response.body()), "$.total").toString());
    }

    private String aesEncrypt(String value, String key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key.getBytes("UTF-8"), "AES"), new IvParameterSpec(
                "0102030405060708".getBytes("UTF-8")));
        return java.util.Base64.getEncoder().encodeToString(cipher.doFinal(value.getBytes()));
    }

    private String rsaEncrypt(String value) throws UnsupportedEncodingException {
        value = new StringBuilder(value).reverse().toString();
        BigInteger valueInt = hexToBigInteger(stringToHex(value));
        BigInteger pubkey = hexToBigInteger("010001");
        BigInteger modulus = hexToBigInteger("00e0b509f6259df8642dbc35662901477df22677ec152b5ff68ace615bb7b725152b3ab17a876aea8a5aa76d2e417629ec4ee341f56135fccf695280104e0312ecbda92557c93870114af6c9d05c4f7f0c3685b7a46bee255932575cce10b424d813cfe4875d3e82047b97ddef52741d546b8e289dc6935b3ece0462db0a22b8e7");
        return valueInt.modPow(pubkey, modulus).toString(16);
    }

    private BigInteger hexToBigInteger(String hex) {
        return new BigInteger(hex, 16);
    }

    private String stringToHex(String text) throws UnsupportedEncodingException {
        return DatatypeConverter.printHexBinary(text.getBytes("UTF-8"));
    }

    //通过歌曲ID获取评论API，网易对其进行了加密
    public static MusicCommentMessage parseCommentMessage(String title, String url) throws Exception {
        String songUrl = url;
        String songId = songUrl.substring(songUrl.indexOf("id=")+3);
        URL uri = new URL(songUrl);
        Document msdoc = Jsoup.parse(uri, 3000);

        /*得到歌曲对应的图片*/
        Elements imgUrls = msdoc.select("img.j-img");
        String imgUrl = new String();
        String imgSrcUrl = new String();
        for(Element e : imgUrls){
            imgUrl = e.attr("src");
            imgSrcUrl = e.attr("data-src");
        }

        String secKey = new BigInteger(100, new SecureRandom()).toString(32).substring(0, 16);
        String encText = EncryptUtils.aesEncrypt(EncryptUtils.aesEncrypt(Constants.TEXT, "0CoJUm6Qyw8W8jud"), secKey);
        String encSecKey = EncryptUtils.rsaEncrypt(secKey);
        Response response = Jsoup
                .connect(Constants.NET_EASE_COMMENT_API_URL + songId + "/?csrf_token=")
                .method(Connection.Method.POST).header("Referer", Constants.BASE_URL)
                .data(ImmutableMap.of("params", encText, "encSecKey", encSecKey)).execute();


        Object res = JSON.parse(response.body());

        if (res == null) {
            return null;
        }

        MusicCommentMessage musicCommentMessage = new MusicCommentMessage();

        int commentCount = (int)JSONPath.eval(res, "$.total");
        int hotCommentCount = (int)JSONPath.eval(res, "$.hotComments.size()");
        int latestCommentCount = (int)JSONPath.eval(res, "$.comments.size()");

        //musicCommentMessage.setSongTitle(msdoc.title());
        musicCommentMessage.setSongTitle(title);
        musicCommentMessage.setSongUrl(songUrl);
        musicCommentMessage.setCommentCount(commentCount);
        /*图片来源*/
        musicCommentMessage.setImgSrcUrl(imgSrcUrl);
        musicCommentMessage.setImgUrl(imgUrl);

        List<MusicComment> ls = new ArrayList<MusicComment>();

        if (commentCount != 0 && hotCommentCount != 0) {

            for (int i = 0; i < hotCommentCount; i++) {
                String nickname = JSONPath.eval(res, "$.hotComments[" + i + "].user.nickname").toString();
                String time = EncryptUtils.stampToDate((long)JSONPath.eval(res, "$.hotComments[" + i + "].time"));
                String content = JSONPath.eval(res, "$.hotComments[" + i + "].content").toString();
                //String appreciation = JSONPath.eval(res, "$.hotComments[" + i + "].likedCount").toString();
                Long appreciation = Long.valueOf(JSONPath.eval(res, "$.hotComments[" + i + "].likedCount").toString()).longValue();
                ls.add(new MusicComment(title,url,"hotComment", nickname, time, content, appreciation,imgUrl,imgSrcUrl));
            }
        } else if (commentCount != 0) {

            for (int i = 0; i < latestCommentCount; i++) {
                String nickname = JSONPath.eval(res, "$.comments[" + i + "].user.nickname").toString();
                String time = EncryptUtils.stampToDate((long)JSONPath.eval(res, "$.comments[" + i + "].time"));
                String content = JSONPath.eval(res, "$.comments[" + i + "].content").toString();
                //String appreciation = JSONPath.eval(res, "$.comments[" + i + "].likedCount").toString();
                Long appreciation = Long.valueOf(JSONPath.eval(res, "$.hotComments[" + i + "].likedCount").toString()).longValue();
                ls.add(new MusicComment(title,url,"latestCommentCount", nickname, time, content, appreciation,imgUrl,imgSrcUrl));
            }
        }

        musicCommentMessage.setComments(ls);

        return musicCommentMessage;
    }
    
    public static <T> void main(String[] args) throws Exception {
        HtmlParser htmlParser = new HtmlParser();
        /*htmlParser.parsePlaylists("http://music.163.com/discover/playlist/?order=hot&cat=%E5%85%A8%E9%83%A8&limit=35&offset=0")
        .forEach(playlist -> System.out.println(playlist));
        System.out.println("=====================");*/
        htmlParser.parsePlaylist("http://music.163.com/playlist?id=454016843").forEach(song -> System.out.println(song));
        //System.out.println("=====================");
        //System.out.println(htmlParser.parseSong("http://music.163.com/song?id=29999506"));
    }
    
    
    
}
