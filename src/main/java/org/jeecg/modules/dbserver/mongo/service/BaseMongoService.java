package org.jeecg.modules.dbserver.mongo.service;


import java.io.Serializable;

import org.jeecg.modules.dbserver.mongo.common.model.BaseModel;

/**
 * 基础CRUD的Service接口
 *
 * @author ：Mc_GuYi
 * @since  ：6/25/2019
 *
 * @param <T> 继承{@link BaseModel}的实例对象
 * @param <ID> ID类型
 */
public interface BaseMongoService<T extends BaseModel, ID extends Serializable> {

    /**
     * 得到实例对象
     *
     * @param id 实例对象的id
     * @return 可能返回null
     */
    T get(ID id);

    /**
     * 保存实例对象
     *
     * @param t 实例对象
     * @return 实例对象id
     */
    ID save(T t);

    /**
     * 更新实例对象（全部字段更新）
     * @param t 实例对象
     * @return 是否成功
     */
    boolean update(T t);

    /**
     * 更新实例对象（局部字段更新）
     * @param t 实例对象
     * @param fieldNames 需要更新的字段列表
     * @return 是否成功
     */
    boolean update(T t, String... fieldNames);

    /**
     * 删除实例对象
     * @param id 实例对象id
     * @return 是否成功
     */
    boolean delete(ID id);
}

