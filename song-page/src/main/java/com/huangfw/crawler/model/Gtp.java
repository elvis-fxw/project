package com.huangfw.crawler.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Gtp {
    @Id
    String url;
    String title;
    String imgUrl;
    String saveAddress;

    public Gtp() {
        super();
    }

    public Gtp(String url, String title, String imgUrl, String saveAddress) {
        this.url = url;
        this.title = title;
        this.imgUrl = imgUrl;
        this.saveAddress = saveAddress;
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

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getSaveAddress() {
        return saveAddress;
    }

    public void setSaveAddress(String saveAddress) {
        this.saveAddress = saveAddress;
    }

    @Override
    public String toString() {
        return "Gtp{" +
                "url='" + url + '\'' +
                ", title='" + title + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", saveAddress='" + saveAddress + '\'' +
                '}';
    }
}
