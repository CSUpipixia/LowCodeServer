package com.lowcode.server.controller;

import com.alibaba.fastjson.JSONObject;
import com.lowcode.server.entity.Page;
import com.lowcode.server.service.PageService;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoException;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping("/page")
@Slf4j
public class PageController {

    @Resource
    private PageService pageService;

    /**
     * 根据id查询单条数据
     * @param _id
     * @return
     */
    @RequestMapping(value = "/id/{_id}",method = GET)
    public Map query(@PathVariable("_id") String _id) {
        Map result = new HashMap(1);
        if(_id==null){
            result.put("code",404);
            result.put("msg","参数错误");
            return result;
        }
        try{
            Page page = pageService.getPageById(_id);
            if(page==null){
                result.put("code",404);
                result.put("msg","没有数据");
            }
            else {
                result.put("code", 200);
                result.put("msg", "查找成功");
                result.put("data", page);
            }
        }catch (MongoException e){
            result.put("error",e);
        }
        return result;
    }
    /**
     * 获取首页
     * @param
     * @return
     */
    @RequestMapping(value = "/home/get",method = GET)
    public Map queryHome() {
        Map result = new HashMap(1);
        try{
            Page page = pageService.getHomePage();
            if(page==null){
                result.put("code",404);
                result.put("msg","没有数据");
            }
            else {
                result.put("code", 200);
                result.put("msg", "查找成功");
                result.put("data", page);
            }
        }catch (MongoException e){
            result.put("error",e);
        }
        return result;
    }
    /**
     * 根据path查询页面
     * @param path
     * @return
     */
    @RequestMapping(value = "/path/{path}",method = GET)
    public Map queryPageByPath(@PathVariable("path") String path) {
        Map result = new HashMap(1);
        if(path==null){
            result.put("code",404);
            result.put("msg","参数错误");
            return result;
        }
        try{
            Page page = pageService.getPageByPath(path);
            if(page==null){
                result.put("code",404);
                result.put("msg","没有数据");
            }
            else {
                result.put("code", 200);
                result.put("msg", "查找成功");
                result.put("data", page);
            }
        }catch (MongoException e){
            result.put("error",e);
        }
        return result;
    }
    /**
     * 新增页面
     * @param map
     * @return
     */
    @RequestMapping(value = "/create",method = POST)
    public Map addPage(@RequestBody Map<String, Object> map) {
        Map result = new HashMap(1);
        String title = map.get("title").toString();
        String path = map.get("path").toString();
        Boolean isHomePage = Boolean.parseBoolean(map.get("isHomePage").toString());
        if(title==null||path==null||isHomePage==null){
            result.put("code",404);
            result.put("msg","参数错误");
            return result;
        }
        try{
            if(!isHomePage&&pageService.getPageList().isEmpty()){
                isHomePage = !isHomePage;
            }
            if(pageService.getPageByPath(path)!=null){
                result.put("code",404);
                result.put("msg","path已存在");
                return result;
            }
            Page page = new Page(title,path,isHomePage);
            pageService.addPage(page);
            String _id = page.get_id();
            if (isHomePage){
                pageService.setHomePage(_id);
            }
            result.put("code",200);
            result.put("msg","添加页面成功!");
            result.put("data",page);
        }catch (MongoException e){
            result.put("error",e);
        }
        return result;
    }
    /**
     * 编辑页面
     * @param map
     * @return
     */
    @RequestMapping(value = "/{_id}/update",method = POST)
    public Map editPage(@PathVariable("_id") String _id,@RequestBody Map<String, Object> map) {
        Map result = new HashMap(1);
        String title = map.get("title").toString();
        String path = map.get("path").toString();
        Boolean isHomePage = Boolean.parseBoolean(map.get("isHomePage").toString());
        if(_id==null||title==null||path==null||isHomePage==null){
            result.put("code",404);
            result.put("msg","参数错误");
            return result;
        }
        try {
            if(pageService.getPageById(_id) == null){
                result.put("code",404);
                result.put("msg","页面不存在");
                return result;
            }
            if(pageService.getPageByPath(path) != null ){
                result.put("code",404);
                result.put("msg","path已存在不可编辑");
                return result;
            }
            if(pageService.getPageById(_id).getHomePage()&&!isHomePage){ //之前为true，修改为false
                if(pageService.getPageList().size() == 1){
                    //若只有这一个页面 则更改失败
                    result.put("code",404);
                    result.put("msg","必须含有一个主页面无法修改");
                    return result;
                }
                else{
                    String new_id = pageService.getPageList().get(0).get_id();
                    if(new_id != _id){
                        pageService.setHomePage(new_id);
                    }
                    else{
                        pageService.setHomePage(pageService.getPageList().get(1).get_id());
                    }
                }
            }
            UpdateResult updateResult = pageService.editPage(_id,title,path,isHomePage);
            if (isHomePage){
                pageService.setHomePage(_id); //其他页面要设为false
            }
            Page page = pageService.getPageById(_id);
            if(updateResult.getUpsertedId() != null || (updateResult.getMatchedCount() > 0 && updateResult.getModifiedCount() > 0)){
                result.put("code",200);
                result.put("msg","保存页面成功");
                result.put("data",page);
            }
            else{
                result.put("code",404);
                result.put("msg","编辑页面失败");
            }
        }catch(MongoException e){
            result.put("error",e);
        }
        return result;
    }

    /**
     * 保存页面
     * @param map
     * @return
     */
    @RequestMapping(value = "/{_id}/save",method = POST)
    public Map savePageData(@PathVariable("_id") String _id,@RequestBody Map<String, JSONObject> map) {
        Map result = new HashMap(1);
        JSONObject pageData = map.get("pageData");
        if(_id==null||pageData==null){
            result.put("code",404);
            result.put("msg","参数错误");
            return result;
        }
        try {
            UpdateResult updateResult = pageService.savePageData(_id,pageData);
            if(updateResult.getUpsertedId() != null || (updateResult.getMatchedCount() > 0 && updateResult.getModifiedCount() > 0)){
                result.put("code",200);
                result.put("msg","保存页面成功");
                result.put("data",pageData);
            }
            else{
                result.put("code",404);
                result.put("msg","保存失败");
            }
        }catch(MongoException e){
            result.put("error",e);
        }
        return result;
    }

    /**
     * 获取页面列表
     * @param
     * @return
     */
    @RequestMapping(value = "/query",method = GET)
    public Map getPageList() {
        List<Page> pageList = null;
        Map result = new HashMap(1);
        try{
            pageList = pageService.getPageList();
            if(pageList.size()==0){
                result.put("code",404);
                result.put("msg","暂无数据");
            }
            else{
                result.put("code",200);
                result.put("msg","获取页面列表成功");
                result.put("data",pageList);
            }
        }catch (MongoException e){
            result.put("error",e);
        }
        return result;
    }

    /**
     * 删除页面
     * @param _id
     * @return
     */
    @RequestMapping(value = "/{_id}/delete",method = POST)
    public Map deletePage(@PathVariable("_id") String _id) {
        Map result = new HashMap(1);
        if(_id==null){
            result.put("code",404);
            result.put("msg","参数错误");
            return result;
        }
        try{
            DeleteResult deleteResult = null;
            if(pageService.getPageById(_id).getIsHomePage()){ //要删除的页面为主页面
                if(pageService.getPageList().size()>1){ //有多个页面
                    deleteResult = pageService.deletePage(_id); //删除该页面
                    Page page = pageService.getPageList().get(0);
                    pageService.setHomePage(page.get_id()); //设第一个页面为主页面
                }
                else{
                    deleteResult = pageService.deletePage(_id);
                }
            }
            else{
                deleteResult = pageService.deletePage(_id);
            }
            if(deleteResult.getDeletedCount()> 0) {
                result.put("code", 200);
                result.put("msg", "删除成功");
            }
            else{
                result.put("code", 404);
                result.put("msg", "删除失败");
            }

        }catch (MongoException e){
            result.put("error",e);
        }
        return result;
    }

    /**
     * 设置主页面
     * @param _id
     * @return
     */
    @RequestMapping(value = "/{_id}/setHomePage",method = POST)
    public Map setHomePage(@PathVariable("_id") String _id) {
        Map result = new HashMap(1);
        if(_id==null){
            result.put("code",404);
            result.put("msg","参数错误");
            return result;
        }
        try {
            UpdateResult updateResult = pageService.setHomePage(_id);
            if(updateResult.getUpsertedId() != null || (updateResult.getMatchedCount() > 0 && updateResult.getModifiedCount() > 0)) {
                result.put("code",200);
                result.put("msg","设置首页成功");
            }
            else{
                result.put("code",404);
                result.put("msg","设置失败");
            }
        }catch (MongoException e){
            result.put("error",e);
        }
        return result;
    }





}
