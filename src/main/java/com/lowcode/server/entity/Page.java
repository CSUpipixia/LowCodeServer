package com.lowcode.server.entity;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;
@Document(collection = "page")
@Data
public class Page {
    @Id
    private String _id;
    @Field
    private String title;
    @Field
    private String path;
    @Field
    private Boolean isHomePage;
    @Field
    private JSONObject pageData;
    @CreatedDate
    private Date createdTime;
    @LastModifiedDate
    private Date updatedTime;

    @Version
    private Long version;

    public Page(){

    }
    public Page(String _id, String title, String path, Boolean isHomePage, JSONObject pageData, Date createdTime, Date updatedTime) {
        this._id = _id;
        this.title = title;
        this.path = path;
        this.isHomePage = isHomePage;
        this.pageData = pageData;
        this.createdTime = createdTime;
        this.updatedTime = updatedTime;
    }

    public Page(String title, String path, Boolean isHomePage) {
        this.title = title;
        this.path = path;
        this.isHomePage = isHomePage;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Boolean getHomePage() {
        return isHomePage;
    }

    public void setHomePage(Boolean homePage) {
        isHomePage = homePage;
    }

    public JSONObject getPageData() {
        return pageData;
    }

    public void setPageData(JSONObject pageData) {
        this.pageData = pageData;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    @Override
    public String toString() {
        return "Page{" +
                "_id='" + _id + '\'' +
                ", title='" + title + '\'' +
                ", path='" + path + '\'' +
                ", isHomePage=" + isHomePage +
                ", pageData=" + pageData +
                ", createdTime=" + createdTime +
                ", updatedTime=" + updatedTime +
                '}';
    }
}
