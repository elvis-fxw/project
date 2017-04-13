package com.huangfw.crawler.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

@Entity
public class GtpWebPage {

    public enum PageType {
        gtp, gtplist;
    }

    public enum Status {
        crawled, uncrawl;
    }

    @Id
    private String url;
    private String title;

    @Enumerated(EnumType.STRING)
    private PageType type;

    @Enumerated(EnumType.STRING)
    private Status status;

    public GtpWebPage() {
        super();
    }

    public GtpWebPage(String url, PageType type) {
        super();
        this.url = url;
        this.type = type;
        this.status = Status.uncrawl;
    }

    public GtpWebPage(String url, PageType type, String title) {
        super();
        this.url = url;
        this.type = type;
        this.title = title;
        this.status = Status.uncrawl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public PageType getType() {
        return type;
    }

    public void setType(PageType type) {
        this.type = type;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "GtpWebPage{" +
                "url='" + url + '\'' +
                ", title='" + title + '\'' +
                ", type=" + type +
                ", status=" + status +
                '}';
    }
}
