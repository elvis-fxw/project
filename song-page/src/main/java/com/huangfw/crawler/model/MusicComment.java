package com.huangfw.crawler.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class MusicComment {

    private String title;

    private String url;

    //评论类型
    private String type;

    private String nickname;

    private String commentDate;

    @Id
    private String content;

    //获赞数
    private Long appreciation;

    private String imgUrl;

    private String imgSrcUrl;

    public MusicComment() {}

    public MusicComment(String title,String url,String type, String nickname, String commentDate, String content, Long appreciation,
                        String imgUrl,String imgSrcUrl) {
        this.title = title;
        this.url = url;
        this.type = type;
        this.nickname = nickname;
        this.commentDate = commentDate;
        this.content = content;
        this.appreciation = appreciation;
        this.imgUrl = imgUrl;
        this.imgSrcUrl = imgSrcUrl;
    }

    public String getImgSrcUrl() {
        return imgSrcUrl;
    }

    public void setImgSrcUrl(String imgSrcUrl) {
        this.imgSrcUrl = imgSrcUrl;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getCommentDate() {
        return commentDate;
    }

    public void setCommentDate(String commentDate) {
        this.commentDate = commentDate;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getAppreciation() {
        return appreciation;
    }

    public void setAppreciation(Long appreciation) {
        this.appreciation = appreciation;
    }

    @Override
    public String toString() {
        return "MusicComment{" +
                "title='" + title + '\'' +
                ", url='" + url + '\'' +
                ", type='" + type + '\'' +
                ", nickname='" + nickname + '\'' +
                ", commentDate='" + commentDate + '\'' +
                ", content='" + content + '\'' +
                ", appreciation=" + appreciation +
                ", imgUrl='" + imgUrl + '\'' +
                ", imgSrcUrl='" + imgSrcUrl + '\'' +
                '}';
    }
}
