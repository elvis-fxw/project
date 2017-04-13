package com.huangfw.crawler.repository;

import com.huangfw.crawler.model.GtpWebPage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GtpWebPageRepository extends JpaRepository<GtpWebPage, String> {

    GtpWebPage findTopByStatus(GtpWebPage.Status status);

    long countByStatus(GtpWebPage.Status status);

    List<GtpWebPage> findAllByType(GtpWebPage.PageType pageType);
}
