package org.jeecg.modules.dbserver.mongo.repository.impl;

import com.github.pagehelper.Page;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.util.Assert;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.jeecg.modules.dbserver.mongo.common.model.BaseModel;
import org.jeecg.modules.dbserver.mongo.common.page.PagingQuery;
import org.jeecg.modules.dbserver.mongo.repository.BaseMongoRepository;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

public class BaseMongoRepositoryImpl<T extends BaseModel, ID extends Serializable>
        implements BaseMongoRepository<T, ID>, InitializingBean {

    private MongoTemplate mongoTemplate;

    private static final ExpressionParser parser = new SpelExpressionParser();

    private Class<T> modelClass;

    private Field idField;

    @Override
    public T get(ID id) {
        if (id == null) {
            return null;
        }
        return mongoTemplate.findById(id, modelClass);
    }

    @Override
    public T getOne(Criteria criteria) {
        return mongoTemplate.findOne(new Query(criteria), modelClass);
    }

    @Override
    public ID save(T t) {
        mongoTemplate.insert(t);
        return getIdValue(t);
    }

    @Override
    public boolean update(T t) {
        mongoTemplate.save(t);
        return true;
    }

    @Override
    public boolean update(T t, String... fieldNames) {
        Query query = new Query(Criteria.where(idField.getName()).is(getIdValue(t)));
        StandardEvaluationContext context = new StandardEvaluationContext(t);

        Update update = new Update();
        for (String name : fieldNames) {
            update.set(name, parser.parseExpression(name).getValue(context));
        }

        UpdateResult result = mongoTemplate.updateFirst(query, update, t.getClass());
        return result.getModifiedCount() > 0;
    }

    @Override
    public boolean delete(Serializable id) {
        Query query = new Query(Criteria.where(idField.getName()).is(id));
        DeleteResult result = mongoTemplate.remove(query, modelClass);
        return result.getDeletedCount() > 0;
    }

    @Override
    public <Q extends PagingQuery> List<T> page(Q pq, CriteriaBuilder<Q> builder) {
        return page(pq, null, builder);
    }

    @Override
    public <Q extends PagingQuery> List<T> page(Q pq, Criteria criteria) {
        return page(pq, null, criteria);
    }

    @Override
    public <Q extends PagingQuery> List<T> page(Q pq, Sort sort, CriteriaBuilder<Q> builder) {
        return page(pq, null, builder.build(pq));
    }

    @Override
    public <Q extends PagingQuery> List<T> page(Q pq, Sort sort, Criteria criteria) {
        Page<T> list = new Page<>();
        // 创建分页查询对象
        Query query = new Query();
        // 设置查询排序条件(创建时间:从高到低)
        if (sort != null) {
            query.with(sort);
        }
        // 设置分页查询条件
        if (criteria != null) {
            query.addCriteria(criteria);
        }
        // 设置分页数量
        query.skip((pq.getCurrentPage() - 1) * pq.getPageSize());
        query.limit(pq.getPageSize());
        // 查询出总条数
        list.setTotal(getMongoTemplate().count(query, modelClass));
        // 查询出内容
        list.addAll(getMongoTemplate().find(query, modelClass));
        return list;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        findModelClass();
        Assert.notNull(this.modelClass, "Model class not found. ");

        findIdField();
        Assert.notNull(this.idField, "No id field found from model [ " + this.modelClass +
                " ], the id field must be annotated by @org.springframework.data.annotation.Id. ");
    }

    protected MongoTemplate getMongoTemplate() {
        return mongoTemplate;
    }

    @Autowired
    public void setMongoTemplate(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @SuppressWarnings("unchecked")
    private void findModelClass() {
        Type genericSuperclass = this.getClass().getGenericSuperclass();
        if (genericSuperclass instanceof ParameterizedType) {
            this.modelClass = (Class<T>) ((ParameterizedType) genericSuperclass).getActualTypeArguments()[0];
        }
    }

    @SuppressWarnings("unchecked")
    private ID getIdValue(T object) {
        try {
            idField.setAccessible(true);
            return (ID) FieldUtils.readField(idField, object);
        } catch (IllegalAccessException e) {
        }
        return null;
    }

    private void findIdField() {
        for (Field field : this.modelClass.getDeclaredFields()) {
            if (field.isAnnotationPresent(Id.class)) {
                idField = field;
            }
        }
    }

}

