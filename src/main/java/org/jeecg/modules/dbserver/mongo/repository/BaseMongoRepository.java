package org.jeecg.modules.dbserver.mongo.repository;

import org.jeecg.modules.dbserver.mongo.common.model.BaseModel;
import org.jeecg.modules.dbserver.mongo.common.page.PagingQuery;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;

import java.io.Serializable;
import java.util.List;

/**
 *  Mongo基础CRUD操作
 *
 * @author ：Mc_GuYi
 * @since  ：6/25/2019
 *
 *   @param <T>  继承{@link BaseModel}实体类型
 *   @param <ID> ID字段类型
 */
public interface BaseMongoRepository<T extends BaseModel, ID extends Serializable> {

    /**
     * 通过ID得到Mongo实例对象
     *
     * @param id 实例对象ID
     * @return 实例对象
     */
    T get(ID id);

    /**
     * 通过条件查询单个实例对象
     *
     * @param criteria
     * @return
     */
    T getOne(Criteria criteria);

    /**
     * 保存Mongo实例对象
     *
     * @param t 被保存的实例对象
     * @return 对象ID
     */
    ID save(T t);

    /**
     * 更新全部字段
     *
     * @param t 被更新的实例对象
     * @return 是否成功
     */
    boolean update(T t);

    /**
     * 根据字段名更新Mongo实例对象，参数为可变长度的数组
     * 支持同时更新多个字段，要求字段名必须完全跟Mongo对象的字段名相同
     *
     * @param t 被更新的实例对象
     * @param fieldNames 被更新对象的字段名
     * @return 是否成功
     */
    boolean update(T t, String... fieldNames);

    /**
     * 通过实例对象的ID删除对象
     *
     * @param id 实例对象ID
     * @return 是否成功
     */
    boolean delete(ID id);

    /**
     * 分页查询
     *
     * @param <Q> {@link PagingQuery}实现类
     * @param pq 分页条件对象
     * @param builder Criteria构造器
     * @return 基于{@link Page}的分页结果集
     * @since 0.5.x
     */
    <Q extends PagingQuery> List<T> page(Q pq, CriteriaBuilder<Q> builder);

    /**
     * 分页查询
     *
     * @param <Q> {@link PagingQuery}实现类
     * @param pq 分页条件对象
     * @param criteria Criteria构造器
     * @return 基于{@link Page}的分页结果集
     * @since 0.5.x
     */
    <Q extends PagingQuery> List<T> page(Q pq, Criteria criteria);

    /**
     * 分页查询
     *
     * @param <Q> {@link PagingQuery}实现类
     * @param pq 分页条件对象
     * @param sort 排序对象
     * @param builder Criteria构造器
     * @return 基于{@link Page}的分页结果集
     * @since 0.5.x
     */
    <Q extends PagingQuery> List<T> page(Q pq, Sort sort, CriteriaBuilder<Q> builder);

    /**
     * 分页查询
     *
     * @param <Q> {@link PagingQuery}实现类
     * @param pq 分页条件对象
     * @param sort 排序对象
     * @param criteria Criteria构造器
     * @return 基于{@link Page}的分页结果集
     * @since 0.5.x
     */
    <Q extends PagingQuery> List<T> page(Q pq, Sort sort, Criteria criteria);

    /**
     * 分页查询条件构造器
     *
     * @author dengqianyong@besttone.com.cn
     * @since  Jun 9, 2019
     *
     */
    public interface CriteriaBuilder<Q extends PagingQuery> {

        /**
         * 构建分页查询的条件
         *
         * @param query 分页条件查询对象
         * @return mongodb用的条件查询对象
         */
        Criteria build(Q query);
    }
}

