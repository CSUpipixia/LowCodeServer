package com.lowcode.server.service;

import com.alibaba.fastjson.JSONObject;
import com.lowcode.server.ServerApplication;
import com.lowcode.server.entity.Page;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoException;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class PageServiceImpl implements PageService{
    @Autowired
    private MongoTemplate mongoTemplate;

    private static Logger logger = Logger.getLogger(ServerApplication.class);

    @Override
    public List<Page> getPageList() {
        List<Page> list= null;
        try {
            list = mongoTemplate.findAll(Page.class);
            logger.info("获取页面列表");
        }catch (MongoException e){
            logger.error(e);
        }
        return list;
    }

    @Override
    public Page getPageById(String _id) {
        Page page = null;
        try {
            Criteria criteria = Criteria.where("_id").is(new ObjectId(_id));
            Query query = new Query();
            query.addCriteria(criteria);
            page = mongoTemplate.findOne(query,Page.class);
            logger.info("查找页面");
        }catch (MongoException e){
            logger.error(e);
        }
        return page;
    }

    @Override
    public Page getPageByPath(String path) {
        Page page = null;
        try {
            Criteria criteria = Criteria.where("path").is(path);
            Query query = new Query();
            query.addCriteria(criteria);
            page = mongoTemplate.findOne(query,Page.class);
            logger.info("查找页面");
        }catch (MongoException e){
            logger.error(e);
        }
        return page;
    }

    @Override
    public UpdateResult editPage(String _id, String title, String path, Boolean isHomePage) {
        UpdateResult updateResult = null;
        try{
            Criteria criteria = Criteria.where("_id").is(new ObjectId(_id));
            Query query = new Query();
            query.addCriteria(criteria);
            Update update = new Update();
            update.set("title", title);
            update.set("path",path);
            update.set("isHomePage",isHomePage);
            updateResult = mongoTemplate.updateFirst(query,update,Page.class);
            logger.info("Update with date Status : " + updateResult.wasAcknowledged());
            logger.info("Number of Record Modified : " + updateResult.getModifiedCount());
        }catch (MongoException e){
            logger.error(e);
        }
        return updateResult;
    }

    @Override
    public UpdateResult savePageData(String _id,JSONObject pageData) {
        UpdateResult updateResult = null;
        try{
            Criteria criteria = Criteria.where("_id").is(new ObjectId(_id));
            Query query = new Query();
            query.addCriteria(criteria);
            Update update = new Update();
            update.set("pageData", pageData);
            updateResult = mongoTemplate.updateFirst(query, update, Page.class);
            logger.info("Update with date Status : " + updateResult.wasAcknowledged());
            logger.info("Number of Record Modified : " + updateResult.getModifiedCount());
        }catch (MongoException e){
            logger.error(e);
        }
        return updateResult;
    }

    @Override
    public void addPage(Page page) {
        try{
            mongoTemplate.insert(page);
            logger.info("新增页面");
        }catch (MongoException e){
            logger.error(e);
        }

    }

    @Override
    public UpdateResult setHomePage(String _id) {
        UpdateResult updateResult = null;
        try {
            Criteria criteria = Criteria.where("_id").is(new ObjectId(_id));
            Query query = new Query();
            query.addCriteria(criteria);
            Criteria criteria2 = Criteria.where("isHomePage").is(true);
            Query query2 = new Query();
            query2.addCriteria(criteria2);
            Update update = new Update();
            update.set("isHomePage", true);
            Update update2 = new Update();
            update2.set("isHomePage", false);
            List<Page> list = getPageList();
            UpdateResult updateResult2 = mongoTemplate.updateMulti(query2,update2,Page.class); //其余页面设为false
            updateResult = mongoTemplate.updateFirst(query, update, Page.class); //该页面设为true
            logger.info("Update with date Status : " + updateResult.wasAcknowledged());
            logger.info("Number of HomePage Modified : " + updateResult2.getModifiedCount());
        }catch (MongoException e){
            logger.error(e);
        }
        return updateResult;
    }

    @Override
    public DeleteResult deletePage(String _id) {
        DeleteResult deleteResult = null;
        try {
            Criteria criteria = Criteria.where("_id").is(new ObjectId(_id));
            Query query = new Query();
            query.addCriteria(criteria);
            deleteResult = mongoTemplate.remove(query, Page.class);
            logger.info("Delete with date Status : " + deleteResult.wasAcknowledged());
            logger.info("Number of Record Deleted : " + deleteResult.getDeletedCount());
        }catch (MongoException e){
            logger.error(e);
        }
        return deleteResult;
    }

    @Override
    public Page getHomePage() {
        Page page = null;
        try {
            Criteria criteria = Criteria.where("isHomePage").is(true);
            Query query = new Query();
            query.addCriteria(criteria);
            page = mongoTemplate.findOne(query,Page.class);
            logger.info("查找首页");
        }catch (MongoException e){
            logger.error(e);
        }
        return page;
    }
}

