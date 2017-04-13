package com.huangfw.crawler.controller;

import com.huangfw.crawler.impl.GtpMultiCrawler;
import com.huangfw.crawler.impl.MultiCrawlerWithJpa;
import com.huangfw.crawler.repository.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.huangfw.crawler.model.Song;

@RestController
@RequestMapping("/jpa")
public class JpaCrawlerController {
    
    @Autowired
    private MultiCrawlerWithJpa musicMultiCrawler;
    
    @Autowired
    private SongRepository songRepository;

    @Autowired
    private GtpMultiCrawler gtpMultiCrawler;
    
    @GetMapping("/start")
    public String start() throws InterruptedException {
        musicMultiCrawler.run();
        return "爬取完毕";
    }

    @GetMapping("/gtpStart")
    public String gtpStart() throws InterruptedException {
        gtpMultiCrawler.run();
        return "爬取吉他谱完毕";
    }
    
    @GetMapping("/songs")
    public Page<Song> songs(Pageable pageable) {
        return songRepository.findAll(pageable);
    }
    
}
