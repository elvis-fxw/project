package com.huangfw.crawler.impl;

import com.huangfw.crawler.Crawler;
import com.huangfw.crawler.GtpCrawler;
import com.huangfw.crawler.GtpHtmlParser;
import com.huangfw.crawler.HtmlParser;
import com.huangfw.crawler.model.*;
import com.huangfw.crawler.repository.GtpRepository;
import com.huangfw.crawler.repository.MusicCommentRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class GtpMultiCrawlerThread implements Runnable {
    @Autowired
    private GtpRepository gtpRepository;

    @Autowired
    private final GtpCrawler gtpMultiCrawler;
    private final GtpHtmlParser htmlParser = new GtpHtmlParser();

    public GtpMultiCrawlerThread(GtpCrawler gtpMultiCrawler) {
        super();
        this.gtpMultiCrawler = gtpMultiCrawler;
    }

    @Override
    public void run() {
        GtpWebPage gtpWebPage;
        int getUnCrawlPageTimes = 0;
        while (true) {
            gtpWebPage = gtpMultiCrawler.getUnCrawlPage();
            if(gtpWebPage == null) {
                if(getUnCrawlPageTimes > 10) {
                    break;
                } else {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    getUnCrawlPageTimes++;
                    continue;
                }
            }
            getUnCrawlPageTimes = 0;
            if(GtpWebPage.PageType.gtplist.equals(gtpWebPage.getType())) {
                gtpMultiCrawler.addToCrawlList(htmlParser.parseGtplist(gtpWebPage.getUrl()));
            } else if(GtpWebPage.PageType.gtp.equals(gtpWebPage.getType())){
                Gtp gtp = htmlParser.parseGtp(gtpWebPage.getUrl(),gtpWebPage.getTitle());
                /*如果抓不到图片的就不保存*/
                if(!gtp.getImgUrl().isEmpty()){
                    System.out.println(gtp.getTitle() + gtp.getImgUrl());
                    gtpMultiCrawler.saveGTP(gtp);
                }
            }
        }
    }
}
