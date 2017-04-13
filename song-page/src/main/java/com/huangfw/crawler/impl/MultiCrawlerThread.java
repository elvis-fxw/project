package com.huangfw.crawler.impl;

import com.huangfw.crawler.model.MusicCommentMessage;
import com.huangfw.crawler.model.WebPage;
import com.huangfw.crawler.repository.MusicCommentRepository;
import com.huangfw.crawler.Crawler;
import com.huangfw.crawler.HtmlParser;
import com.huangfw.crawler.model.MusicComment;
import com.huangfw.crawler.model.Song;
import org.springframework.beans.factory.annotation.Autowired;

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
                Song song = new Song(webPage.getUrl(), webPage.getTitle(), htmlParser.parseSong(webPage.getUrl()));
                try {
                    MusicCommentMessage mcm = htmlParser.parseCommentMessage(webPage.getTitle(),webPage.getUrl());
                    List<MusicComment> mc = mcm.getComments();
                    multiCrawler.saveMusicComments(mc);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println(webPage.getUrl()+" "+webPage.getTitle() + "抓取评论失败");
                }
                multiCrawler.saveSong(song);
            }
        }
    }

}
