package com.huangfw.crawler.impl;

import com.huangfw.crawler.model.MusicCommentMessage;
import com.huangfw.crawler.model.WebPage;
import com.huangfw.crawler.repository.MusicCommentRepository;
import com.huangfw.crawler.Crawler;
import com.huangfw.crawler.HtmlParser;
import com.huangfw.crawler.model.MusicComment;
import com.huangfw.crawler.model.Song;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

import static com.huangfw.crawler.utils.Constants.COLLECT_WEIGHT;
import static com.huangfw.crawler.utils.Constants.COMMENTS_WEIGHT;

public class MultiCrawlerThread implements Runnable {
    @Autowired
    private MusicCommentRepository musicCommentRepository;

    private final Crawler multiCrawler;
    private final HtmlParser htmlParser = new HtmlParser();

    public MultiCrawlerThread(Crawler multiCrawler) {
        super();
        this.multiCrawler = multiCrawler;
    }
    
    @Override
    public void run() {
        WebPage webPage;
        int getUnCrawlPageTimes = 0;
        while (true) {
            webPage = multiCrawler.getUnCrawlPage();
            if(webPage == null) {
                if(getUnCrawlPageTimes > 10) {
                    break;
                } else {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    getUnCrawlPageTimes++;
                    continue;
                }
            }
            getUnCrawlPageTimes = 0;
            if(WebPage.PageType.playlists.equals(webPage.getType())) {
                multiCrawler.addToCrawlList(htmlParser.parsePlaylists(webPage.getUrl()));
            } else if(WebPage.PageType.playlist.equals(webPage.getType())) {
                multiCrawler.addToCrawlList(htmlParser.parsePlaylist(webPage.getUrl()));
            } else if(WebPage.PageType.song.equals(webPage.getType())){
                /*获取评论和点赞数*/
                int appreciationSum = 0;
                int commentPeopleCount = 0;
                String imgUrl = new String();
                String imgSrcUrl = new String();
                try {
                    MusicCommentMessage mcm = htmlParser.parseCommentMessage(webPage.getTitle(),webPage.getUrl());
                    /*得到歌曲图片*/
                    imgUrl = mcm.getImgUrl();
                    imgSrcUrl = mcm.getImgSrcUrl();

                    List<MusicComment> mc = mcm.getComments();
                    /*热评人数*/
                    commentPeopleCount = mc.size();
                    /*计算点赞数的和*/
                    for(MusicComment musicComment : mc){
                        appreciationSum+=musicComment.getAppreciation();
                        try {
                            multiCrawler.saveMusicComment(musicComment);
                        } catch (Exception e) {
                            e.printStackTrace();
                            System.out.println(webPage.getTitle()+" 单条评论保存失败");
                        }
                    }
                    //决定不采取一起保存的格式，而是单个保存的格式，避免有异常时全都没保存
                    //multiCrawler.saveMusicComments(mc);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println(webPage.getUrl()+" "+webPage.getTitle() + "抓取评论失败");
                }
                double recommendValue = 0;
                double collectCount = webPage.getCollectCount();
                double playCount = webPage.getPlayCount();
                if(commentPeopleCount==0){
                    try {
                        if(playCount!=0){
                            recommendValue = (collectCount/playCount)*(COMMENTS_WEIGHT*commentPeopleCount+COLLECT_WEIGHT*collectCount);
                        }else{
                            recommendValue = 0.01*(COMMENTS_WEIGHT*commentPeopleCount+COLLECT_WEIGHT*collectCount);
                        }
                        //得到两位数的评论值
                        BigDecimal bg = new BigDecimal(recommendValue);
                        recommendValue = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println("计算《"+webPage.getTitle()+"》推荐值失败");
                    }
                }else{
                    try {
                        double averageAppreciation = (double) appreciationSum / commentPeopleCount;
                        if(playCount!=0){
                            recommendValue = (collectCount/playCount)*(averageAppreciation+COMMENTS_WEIGHT*commentPeopleCount+COLLECT_WEIGHT*collectCount);
                        }else{
                            recommendValue = 0.01*(averageAppreciation+COMMENTS_WEIGHT*commentPeopleCount+COLLECT_WEIGHT*collectCount);
                        }
                        //得到两位数的评论值
                        BigDecimal bg = new BigDecimal(recommendValue);
                        recommendValue = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println("计算《"+webPage.getTitle()+"》推荐值失败");
                    }
                }
                Song song = new Song(webPage.getUrl(), webPage.getTitle(), htmlParser.parseSong(webPage.getUrl()),imgUrl,
                        imgSrcUrl,webPage.getTag(),recommendValue);
                multiCrawler.saveSong(song);
            }
        }
    }

}
