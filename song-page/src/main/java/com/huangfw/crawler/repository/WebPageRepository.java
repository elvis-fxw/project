package com.huangfw.crawler.repository;

import com.huangfw.crawler.model.WebPage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WebPageRepository extends JpaRepository<WebPage, String> {

    WebPage findTopByStatus(WebPage.Status status);
    
    long countByStatus(WebPage.Status status);

    List<WebPage> findAllByType(WebPage.PageType pageType);
}
