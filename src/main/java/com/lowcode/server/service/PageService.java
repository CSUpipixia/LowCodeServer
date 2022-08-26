package com.lowcode.server.service;

import com.alibaba.fastjson.JSONObject;
import com.lowcode.server.entity.Page;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

import java.util.List;

public interface PageService {

    /**
     * 根据json字符串，新增页面
     * @param  page
     * @return
     */
    public void addPage(Page page);
    /**
     * 获取所有页面
     * @param
     * @return List<Page>
     */
    public List<Page> getPageList();
    /**
     * 保存页面数据
     * @param
     * @return
     */
    public UpdateResult savePageData(String _id, JSONObject pageData);
    /**
     * 编辑页面
     * @param _id,title,path,isHomePage
     * @return Page
     */
    public UpdateResult editPage(String _id,String title,String path,Boolean isHomePage);
    /**
     * 根据id获取页面
     * @param _id
     * @return Page
     */
    public Page getPageById(String _id);
    /**
     * 获取首页
     * @param
     * @return Page
     */
    public Page getHomePage();
    /**
     * 根据path获取页面
     * @param path
     * @return Page
     */
    public Page getPageByPath(String path);
    /**
     * 设置主页面
     * @param _id isHomePage
     * @return Page
     */
    public UpdateResult setHomePage(String _id);
    /**
     * 根据id 删除页面
     * @param _id
     * @return
     */
    public DeleteResult deletePage(String _id);
}
