package com.huangfw.crawler.impl;

import com.huangfw.crawler.GtpCrawler;
import com.huangfw.crawler.model.Gtp;
import com.huangfw.crawler.model.GtpWebPage;
import com.huangfw.crawler.model.Song;
import com.huangfw.crawler.model.WebPage;
import com.huangfw.crawler.repository.GtpRepository;
import com.huangfw.crawler.repository.GtpWebPageRepository;
import com.huangfw.crawler.repository.WebPageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Component
public class GtpMultiCrawler implements GtpCrawler {
    public static final Integer MAX_THREADS = 20;

    @Autowired
    private GtpWebPageRepository gtpWebPageRepository;

    @Autowired
    private GtpRepository gtpRepository;

    @Override
    public void initCrawlerList() {
        gtpWebPageRepository.saveAndFlush(new GtpWebPage("http://www.jitashe.net/guide/hottab/t1/", GtpWebPage.PageType.gtplist, "热门吉他谱"));
    }

    @Override
    public GtpWebPage getUnCrawlPage() {
        GtpWebPage gtpWebPage = gtpWebPageRepository.findTopByStatus(GtpWebPage.Status.uncrawl);
        if(gtpWebPage == null) {
            return null;
        }
        gtpWebPage.setStatus(GtpWebPage.Status.crawled);
        return gtpWebPageRepository.saveAndFlush(gtpWebPage);
    }

    @Override
    public List<GtpWebPage> addToCrawlList(List<GtpWebPage> gtpWebPages) {
        gtpWebPages = gtpWebPageRepository.save(gtpWebPages);
        gtpWebPageRepository.flush();
        return gtpWebPages;
    }

    @Override
    public Gtp saveGTP(Gtp gtp) {
        return gtpRepository.saveAndFlush(gtp);
    }

    @Override
    public List<Gtp> getGTPs() {
        return gtpRepository.findAll();
    }

    @Override
    public void doRun() {
        ExecutorService executorService = Executors.newFixedThreadPool(MAX_THREADS);
        for(int i = 0; i < MAX_THREADS; i++) {
            executorService.execute(new GtpMultiCrawlerThread(this));
        }
        executorService.shutdown();
        try {
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
