package com.huangfw.crawler.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Song {
    
    @Id
    private String url;
    private String title;
    private Long commentCount;



    private String imgUrl;
    private String imgSrcUrl;

    private String tag;

    double recommendValue;
    
    public Song() {
        super();
    }
    
    public Song(String url, String title, Long commentCount) {
        super();
        this.url = url;
        this.title = title;
        this.commentCount = commentCount;
    }

    /*还没有爬取歌曲评论数的时候保存*/
    /*public Song(String url, String title, String tag) {
        this.url = url;
        this.title = title;
        this.tag = tag;
    }*/

    public Song(String url, String title, Long commentCount, String tag) {
        this.url = url;
        this.title = title;
        this.commentCount = commentCount;
        this.tag = tag;
    }

    public Song(String url, String title, Long commentCount, String tag, double recommendValue) {
        this.url = url;
        this.title = title;
        this.commentCount = commentCount;
        this.tag = tag;
        this.recommendValue = recommendValue;
    }

    public Song(String url, String title, Long commentCount, String imgUrl, String imgSrcUrl, String tag, double recommendValue) {
        this.url = url;
        this.title = title;
        this.commentCount = commentCount;
        this.imgUrl = imgUrl;
        this.imgSrcUrl = imgSrcUrl;
        this.tag = tag;
        this.recommendValue = recommendValue;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getImgSrcUrl() {
        return imgSrcUrl;
    }

    public void setImgSrcUrl(String imgSrcUrl) {
        this.imgSrcUrl = imgSrcUrl;
    }

    public double getRecommendValue() {
        return recommendValue;
    }

    public void setRecommendValue(double recommendValue) {
        this.recommendValue = recommendValue;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Long commentCount) {
        this.commentCount = commentCount;
    }

    @Override
    public String toString() {
        return "Song{" +
                "url='" + url + '\'' +
                ", title='" + title + '\'' +
                ", commentCount=" + commentCount +
                ", tag='" + tag + '\'' +
                '}';
    }
}
