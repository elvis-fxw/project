package com.huangfw.crawler;

import com.huangfw.crawler.impl.GtpMultiCrawler;
import com.huangfw.crawler.model.GtpWebPage;
import com.huangfw.crawler.model.WebPage;
import com.huangfw.crawler.repository.GtpWebPageRepository;
import com.huangfw.crawler.repository.WebPageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.huangfw.crawler.impl.MultiCrawlerWithJpa;

import java.util.List;

@Component
public class UpdateSchedule {

    @Autowired
    private MultiCrawlerWithJpa multiCrawler;

    @Autowired
    private WebPageRepository webPageRepository;

    @Autowired
    private GtpMultiCrawler gtpMultiCrawler;

    @Autowired
    private GtpWebPageRepository gtpWebPageRepository;

    @Scheduled(cron = "0 0 1 * * *")
    public void update() {
        List<WebPage> webPages = webPageRepository.findAllByType(WebPage.PageType.song);
        webPages.forEach(p -> p.setStatus(WebPage.Status.uncrawl));
        webPageRepository.save(webPages);
        multiCrawler.doRun();

        List<GtpWebPage> gtpWebPages = gtpWebPageRepository.findAllByType(GtpWebPage.PageType.gtplist);
        gtpWebPages.forEach(p -> p.setStatus(GtpWebPage.Status.uncrawl));
        gtpWebPageRepository.save(gtpWebPages);
        gtpMultiCrawler.doRun();
    }
}
