package com.huangfw.crawler.impl;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.huangfw.crawler.Crawler;
import com.huangfw.crawler.model.WebPage;
import com.huangfw.crawler.repository.MusicCommentRepository;
import com.huangfw.crawler.repository.SongRepository;
import com.huangfw.crawler.repository.WebPageRepository;
import com.huangfw.crawler.model.MusicComment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.huangfw.crawler.model.Song;

@Component
public class MultiCrawlerWithJpa implements Crawler {
    
    public static final Integer MAX_THREADS = 40;//20
    
    @Autowired
    private WebPageRepository webPageRepository;
    
    @Autowired
    private SongRepository songRepository;

    @Autowired
    private MusicCommentRepository musicCommentRepository;
    
    @Override
    public void initCrawlerList() {
        final int PAGE_COUNT = 41;//41
        for(int i = 0; i < PAGE_COUNT; i++) {
            webPageRepository.saveAndFlush(new WebPage("http://music.163.com/discover/playlist/?order=hot&cat=%E5%85%A8%E9%83%A8&limit=35&offset="  + (i * 35), WebPage.PageType.playlists));
        }
        //webPageRepository.saveAndFlush(new WebPage("http://music.163.com/playlist?id=454016843", WebPage.PageType.playlist));
    }

    @Override
    public synchronized WebPage getUnCrawlPage() {
        WebPage webPage = webPageRepository.findTopByStatus(WebPage.Status.uncrawl);
        if(webPage == null) {
            return null;
        }
        webPage.setStatus(WebPage.Status.crawled);
        //System.out.println("开始爬取歌单"+webPage.getTitle());
        return webPageRepository.saveAndFlush(webPage);
    }
    
    @Override
    public List<WebPage> addToCrawlList(List<WebPage> webPages) {
        webPages = webPageRepository.save(webPages);
        webPageRepository.flush();
        return webPages;
    }

    @Override
    public Song saveSong(Song song) {
        return songRepository.saveAndFlush(song);
    }

    @Override
    public MusicComment saveMusicComment(MusicComment musicComment){
        return musicCommentRepository.saveAndFlush(musicComment);
    }

    @Override
    public List<MusicComment> saveMusicComments(List<MusicComment> musicComments){
        musicComments =  musicCommentRepository.save(musicComments);
        musicCommentRepository.flush();
        return musicComments;
    }

    @Override
    public List<Song> getSongs() {
        return songRepository.findAll();
    }

    @Override
    public void doRun(){
        ExecutorService executorService = Executors.newFixedThreadPool(MAX_THREADS);
        for(int i = 0; i < MAX_THREADS; i++) {
            executorService.execute(new MultiCrawlerThread(this));
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
