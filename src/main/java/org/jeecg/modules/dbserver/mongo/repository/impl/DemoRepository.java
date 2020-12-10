package org.jeecg.modules.dbserver.mongo.repository.impl;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.result.DeleteResult;

import org.bson.Document;
import org.jeecg.modules.dbserver.mongo.common.model.Alarm;
import org.jeecg.modules.dbserver.mongo.common.model.Audit;
import org.jeecg.modules.dbserver.mongo.common.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
//import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 *  UserRepositoryImpl.java
 *
 *  @author ：machengxin
 *  @since  ：6/11/2019-2:56 PM
 */
@Repository
public class DemoRepository {

    @Autowired
    MongoTemplate mongoTemplate;

    /** 新增数据：根据ID判断：数据不存在执行插入，数据存在执行更新 */
    public void save(User user) {
        mongoTemplate.save(user);
    }

    /** 新增数据：根据ID判断：数据不存在执行插入，数据存在报错 */
    public void insert(User user) {
        mongoTemplate.insert(user);
    }
    public void insertAlarm(Alarm alarm) {
        mongoTemplate.insert(alarm);
    }
    public void insertAudit(Audit audit) {
        mongoTemplate.insert(audit);
    }

    /** 新增数据：批量插入 */
    public void insertAll(List<User> list) {
        mongoTemplate.insertAll(list);
    }

    /** 删除数据：根据传入对象的ID进行删除 */
    public boolean remove(User user) {
        DeleteResult remove = mongoTemplate.remove(user);
        return remove.getDeletedCount() > 0;
    }

    /** 删除数据：根据传入的筛选条件进行删除 */
    public boolean remove(String id) {
        Criteria criteria = new Criteria();
        criteria.and("id").is(id);

        Query query = new Query(criteria);
        DeleteResult remove = mongoTemplate.remove(query, User.class);
        return remove.getDeletedCount() > 0;
    }

    /** 判断是否存在 */
    public boolean exists(){
        Criteria criteria = new Criteria();
        criteria.and("friend").is("xiaoming");
        Query query = new Query(criteria);
        return mongoTemplate.exists(query, User.class);
    }

    /** 查询数据：根据传入的筛选条件进行查询第一个符合条件的数据*/
    public User findOne(){
        Criteria criteria = new Criteria();

        Query query = new Query(criteria);
        return mongoTemplate.findOne(query, User.class);
    }

    /** 查询数据：根据传入的ID进行查询*/
    public User findById(){
        return mongoTemplate.findById("", User.class);
    }

    /** 查询并更新数据：根据传入的筛选条件进行查询，并更新数据,返回更新前的数据 */
    public User findAndModify(){
        //拼接查询条件
        Criteria criteria = new Criteria();
        criteria.and("gender").is("MALE");

        //拼接更新内容
        Update update = new Update();
        update.set("friend","xiaoming");

        Query query = new Query(criteria);
        return mongoTemplate.findAndModify(query, update, User.class);
    }

    /** 查询并删除数据：根据传入的筛选条件进行查询 第一个符合条件的数据，返回并删除数据 */
    public User findAndRemove(Integer age) {
        Criteria criteria = new Criteria();
        criteria.and("age").is(age);

        Query query = new Query(criteria);
        return  mongoTemplate.findAndRemove(query, User.class);

    }

    /** 查询数据：根据传入的筛选条件进行查询 */
    public List<User> find(){
        Criteria criteria = new Criteria();

        Query query = new Query(criteria);
        return mongoTemplate.find(query, User.class);
    }

    /** 查询数据：查询全部数据 */
    public List<User> findAll(){
        return mongoTemplate.findAll(User.class);
    }
    /** 查询数据：查询全部数据 */
    public List<Audit> findAllAudit(){
        return mongoTemplate.findAll(Audit.class);
    }

    /** 查询并删除数据：根据传入的筛选条件进行查询 全部符合条件的数据，返回并删除数据 */
    public List<User> findAllAndRemove(Integer age){
        Criteria criteria = new Criteria();
        criteria.and("age").is(age);

        Query query = new Query(criteria);
        List<User> list = mongoTemplate.findAllAndRemove(query, User.class);
        return list;
    }
    
    public void backupCollection() {
    	List<DBObject> ops = new ArrayList<DBObject>(); 
    	ops.add(new BasicDBObject("$out","target"+System.currentTimeMillis())); // write to collection"target"
    	MongoCollection<Document> source = mongoTemplate.getCollection("t_user");
    	FindIterable<Document> findIterable = source.find();
    	MongoCursor<Document> mongoCursor = findIterable.iterator();
        List<Document> documents = new ArrayList<>();
        while (mongoCursor.hasNext()) {
            documents.add(mongoCursor.next());
        }
        
        String collection=new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        boolean collectionExist=false;
        Set<String> collectionNames = mongoTemplate.getCollectionNames();
        for (final String name : collectionNames) {
            if (name.equalsIgnoreCase(collection)) {
                collectionExist=true;
            }
        }
        if(!collectionExist) {
        	MongoCollection<Document> target=mongoTemplate.createCollection(collection);
        	target.insertMany(documents);
        }
//    	mongoTemplate.dropCollection("t_user");
    }

	public List<User> findByTime() {
//        Criteria criteria = new Criteria();
//        Query query = new Query(criteria);
//        return mongoTemplate.find(query, User.class);

		Aggregation agg;
	 
		agg = Aggregation.newAggregation(
	//			Aggregation.match(Criteria.where("type").is(0)),
				Aggregation.sort(new Sort(Sort.Direction.DESC, "uDate")),
				Aggregation.group("name").
				first("name").as("name").
				first("uDate").as("uDate").
				first("age").as("age")
			);
		AggregationResults<User> results = mongoTemplate.aggregate(agg,"t_user",User.class);
		List<User> list = results.getMappedResults();
		return list;

	}

}
