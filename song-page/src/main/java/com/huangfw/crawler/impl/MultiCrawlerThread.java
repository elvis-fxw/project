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
                try {
                    MusicCommentMessage mcm = htmlParser.parseCommentMessage(webPage.getTitle(),webPage.getUrl());
                    List<MusicComment> mc = mcm.getComments();
                    /*热评人数*/
                    commentPeopleCount = mc.size();
                    /*计算点赞数的和*/
                    for(MusicComment musicComment : mc){
                        appreciationSum+=musicComment.getAppreciation();
                    }
                    multiCrawler.saveMusicComments(mc);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println(webPage.getUrl()+" "+webPage.getTitle() + "抓取评论失败");
                }
                double recommendValue;
                if(commentPeopleCount==0){
                    recommendValue=0;
                }else{
                    double averageAppreciation = (double) appreciationSum / commentPeopleCount;
                    BigDecimal bg = new BigDecimal(averageAppreciation);
                    recommendValue = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                }
                Song song = new Song(webPage.getUrl(), webPage.getTitle(), htmlParser.parseSong(webPage.getUrl()),webPage.getTag(),recommendValue);
                multiCrawler.saveSong(song);
            }
        }
    }

}
