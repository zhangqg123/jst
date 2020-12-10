package org.jeecg.modules.dbserver.mongo.common.utils;

import org.jeecg.modules.dbserver.mongo.common.model.BaseModel;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;

/**
 *  操作Mongo的更新语句工具类
 *  用来生成对应的Update对象
 *
 *  @author ：Mc_GuYi
 *  @since  ：6/25/2019-9:00 AM
 */
public class MongoUpdateUtils {

    /**
     * 构建mongo collection基础字段的更新操作语句
     *
     * @param <M> {@link BaseModel}继承类
     * @param m model对象
     * @param skipNullValue 是否跳过空值对象
     * @return Mongo的Update对象
     */
    public static <M extends BaseModel> Update updateBase(M m, boolean skipNullValue) {
        return update(m, true, true, skipNullValue);
    }

    /**
     * 构建mongo collection内部类的更新操作语句
     * @param <M> {@link BaseModel}继承类
     * @param m model对象
     * @param innerFieldPrefix 内部类的字段前缀，ex: "address.$."
     * @param skipNullValue 是否跳过空值对象
     * @return
     */
    public static <M extends BaseModel> Update updateInner(M m, String innerFieldPrefix, boolean skipNullValue) {
        return update(m, innerFieldPrefix, true, true, skipNullValue);
    }

    /**
     * 构建Mongo的更新操作语句
     *
     * @param <M> {@link BaseModel}继承类
     * @param m model对象
     * @param skipCollection 是否跳过集合对象
     * @param skipInnerModel 是否跳过继承{@link BaseModel}的内部对象
     * @param skipNullValue  是否跳过空值对象
     * @return Mongo的Update对象
     */
    public static <M extends BaseModel> Update update(M m, boolean skipCollection, boolean skipInnerModel, boolean skipNullValue) {
        return update(m, "", skipCollection, skipInnerModel, skipNullValue);
    }

    private static <M extends BaseModel> Update update(M m, String innerFieldPrefix, boolean skipCollection, boolean skipInnerModel, boolean skipNullValue) {
        Objects.requireNonNull(m, "model object can not be null. ");

        Update update = new Update();
        ReflectionUtils.doWithFields(m.getClass(),
                field -> update.set(innerFieldPrefix + field.getName(), ReflectionUtils.getField(field, m)),
                field -> {
                    ReflectionUtils.makeAccessible(field);

                    // 跳过静态变量以及常量
                    if (Modifier.isStatic(field.getModifiers()) || Modifier.isFinal(field.getModifiers())) {
                        return false;
                    }

                    // 跳过内部类，防止栈溢出
                    if (field.getName().startsWith("this$")) {
                        return false;
                    }

                    // 跳过集合
                    if (skipCollection) {
                        if (Collection.class.isAssignableFrom(field.getType())
                                || Map.class.isAssignableFrom(field.getType())) {
                            return false;
                        }
                    }

                    // 跳过内部继承BaseModel的对象
                    if (skipInnerModel) {
                        if (BaseModel.class.isAssignableFrom(field.getType())) {
                            return false;
                        }
                    }

                    // 过滤掉字段为null的值
                    if (ReflectionUtils.getField(field, m) == null) {
                        return false;
                    }
                    return true;
                });

        return update;
    }
}
