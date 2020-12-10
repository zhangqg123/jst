package org.jeecg.modules.dbserver.mongo.service.impl;

import org.jeecg.modules.dbserver.mongo.common.model.BaseModel;
import org.jeecg.modules.dbserver.mongo.repository.BaseMongoRepository;
import org.jeecg.modules.dbserver.mongo.service.BaseMongoService;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;

/**
 *  MongoDB基础的操作类
 *
 *  @author ：Mc_GuYi
 *  @since  ：6/25/2019
 *
 *  @param <R>  实现{@link BaseMongoRepository}的实现类
 *  @param <T>  继承{@link BaseModel}实体类型
 *  @param <ID> ID字段类型
 */
public class BaseMongoServiceImpl<R extends BaseMongoRepository<T, ID>, T extends BaseModel, ID extends Serializable>
        implements BaseMongoService<T, ID> {

    @Autowired
    protected R repository;

    @Override
    public T get(ID id) {
        return repository.get(id);
    }

    @Override
    public ID save(T t) {
        return repository.save(t);
    }

    @Override
    public boolean update(T t) {
        return repository.update(t);
    }

    @Override
    public boolean update(T t, String... fieldNames) {
        return repository.update(t, fieldNames);
    }

    @Override
    public boolean delete(ID id) {
        return repository.delete(id);
    }

}
