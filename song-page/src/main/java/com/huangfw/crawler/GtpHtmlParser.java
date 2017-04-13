package com.huangfw.crawler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONPath;
import com.google.common.collect.ImmutableMap;
import com.huangfw.crawler.model.*;
import com.huangfw.crawler.utils.Constants;
import com.huangfw.crawler.utils.EncryptUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringEscapeUtils;

public class GtpHtmlParser {
    private static final HtmlFetcher HTML_FETCHER = new HtmlFetcher();
    private static final String BASE_URL = "http://www.jitashe.net";
    public static final String SAVE_PATH = "./src/main/resources/static/gtpImage/";

    public List<GtpWebPage> parseGtplist(String url) {
        Elements gtps = Jsoup.parse(HTML_FETCHER.fetch(url)).select("span.xst a");
        //吉他谱名称有一些有转义字符，有一些有%，这时候读取到网页上失败，所以要转换一下
        return gtps.stream().map(e -> new GtpWebPage(BASE_URL + e.attr("href"),
                GtpWebPage.PageType.gtp, StringEscapeUtils.unescapeHtml4(e.html()).replaceAll("%",""))).collect(Collectors.toList());
    }

    public Gtp parseGtp(String url, String title) {
        Elements gtps = Jsoup.parse(HTML_FETCHER.fetch(url)).select("div.alphaTab img");
        String imgUrl = new String();
        String saveName = title + ".png";
        String saveAddress = "gtpImage/" + saveName;
        for(Element e : gtps){
            imgUrl = BASE_URL + e.attr("src");
        }
        /*下载图片*/
        try {
            download(imgUrl,saveName,SAVE_PATH);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getCause());
        }
        Gtp gtp = new Gtp(url,title,imgUrl,saveAddress);
        return gtp;
    }

    public static void download(String urlString, String filename,String savePath) throws Exception {
        try {
            // 构造URL
            URL url = new URL(urlString);
            // 打开连接
            URLConnection con = url.openConnection();
            //设置请求超时为5s
            con.setConnectTimeout(5*1000);
            // 输入流
            InputStream is = con.getInputStream();

            // 1K的数据缓冲
            byte[] bs = new byte[1024];
            // 读取到的数据长度
            int len;
            // 输出的文件流
            File sf=new File(savePath);
            if(!sf.exists()){
                sf.mkdirs();
            }

            File image = new File(savePath + "/" + filename);
        /*如果文件不存在则下载*/
            if(!image.exists()){
            OutputStream os = new FileOutputStream(sf.getPath()+"\\"+filename);
            // 开始读取
            while ((len = is.read(bs)) != -1) {
                os.write(bs, 0, len);
            }
            // 完毕，关闭所有链接
            os.close();
            }
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(filename + "下载失败:" + e.getCause());
        }
    }

    public static <T> void main(String[] args) throws Exception {
        GtpHtmlParser gtpHtmlParser = new GtpHtmlParser();
        /*gtpHtmlParser.parseGtplist("http://www.jitashe.net/guide/hottab/t1/")
                .forEach(gtpList -> System.out.println(gtpList));
        System.out.println("=====================");*/
        System.out.println(gtpHtmlParser.parseGtp("http://www.jitashe.net/tab/55299/","东京食尸鬼(东京喰种) - Unravel"));
    }
}
