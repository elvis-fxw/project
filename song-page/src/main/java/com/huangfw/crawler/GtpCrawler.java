package com.huangfw.crawler;

import com.huangfw.crawler.model.Gtp;
import com.huangfw.crawler.model.GtpWebPage;
import com.huangfw.crawler.model.WebPage;

import java.util.List;

public interface GtpCrawler {
    /**
     * 初始化爬虫队列
     */
    void initCrawlerList();

    /**
     * 获取一个未爬取页面，并将其标记为已爬
     * @return
     */
    GtpWebPage getUnCrawlPage();

    /**
     * 添加页面至爬虫列表
     */
    List<GtpWebPage> addToCrawlList(List<GtpWebPage> gtpWebPages);

    /**
     * 添加歌曲至已爬吉他谱列表
     */
    Gtp saveGTP(Gtp gtp);

    /**
     * 获取所有已爬吉他谱
     */
    List<Gtp> getGTPs();

    /**
     * 获取未爬页面->获取html->解析html并对结果进行处理->标记页面
     * 即流程图右下角黑框部分
     */
    void doRun();

    /**
     * 运行爬虫整体流程
     */
    default void run() {
        initCrawlerList();
        doRun();
    }
}
