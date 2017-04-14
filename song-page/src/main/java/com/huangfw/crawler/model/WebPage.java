package com.huangfw.crawler.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

@Entity
public class WebPage {
    
    public enum PageType {
        song, playlist, playlists;
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

    private Long collectCount;
    private Long shareCount;
    private Long commentCountList;
    private Long playCount;
    private String tag;
    
    public WebPage() {
        super();
    }
    
    public WebPage(String url, PageType type) {
        super();
        this.url = url;
        this.type = type;
        this.status = Status.uncrawl;
    }
    
    public WebPage(String url, PageType type, String title) {
        super();
        this.url = url;
        this.type = type;
        this.title = title;
        this.status = Status.uncrawl;
    }

    public WebPage(String url, PageType type, String title, Long collectCount, Long shareCount,
                   Long commentCountList, Long playCount, String tag) {
        this.url = url;
        this.title = title;
        this.collectCount = collectCount;
        this.shareCount = shareCount;
        this.commentCountList = commentCountList;
        this.playCount = playCount;
        this.tag = tag;
        this.type = type;
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

    public Long getCollectCount() {
        return collectCount;
    }

    public void setCollectCount(Long collectCount) {
        this.collectCount = collectCount;
    }

    public Long getShareCount() {
        return shareCount;
    }

    public void setShareCount(Long shareCount) {
        this.shareCount = shareCount;
    }

    public Long getCommentCountList() {
        return commentCountList;
    }

    public void setCommentCountList(Long commentCountList) {
        this.commentCountList = commentCountList;
    }

    public Long getPlayCount() {
        return playCount;
    }

    public void setPlayCount(Long playCount) {
        this.playCount = playCount;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    public String toString() {
        return "WebPage{" +
                "url='" + url + '\'' +
                ", title='" + title + '\'' +
                ", type=" + type +
                ", status=" + status +
                ", collectCount=" + collectCount +
                ", shareCount=" + shareCount +
                ", commentCountList=" + commentCountList +
                ", playCount=" + playCount +
                ", tag='" + tag + '\'' +
                '}';
    }
}
