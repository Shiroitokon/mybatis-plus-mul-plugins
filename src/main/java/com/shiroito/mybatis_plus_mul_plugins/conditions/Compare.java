package com.shiroito.mybatis_plus_mul_plugins.conditions;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;

import java.io.Serializable;

/**
 * @author hongyq (修改代码请联系开发者)
 * @date 2021-04-30 9:19
 */
public interface Compare<Children> extends Serializable {




    /**
     * ignore
     */
    default <T> Children eq(Class<T> clzz, SFunction<T, ?> column, Object val) {
        return eq(clzz, true, column, val);
    }

    /**
     * 等于 =
     *
     * @param condition 执行条件
     * @param column    字段
     * @param val       值
     * @return children
     */
    <T> Children eq(Class<T> clzz, boolean condition, SFunction<T, ?> column, Object val);

    /**
     * ignore
     */
    default <T> Children ne(Class<T> clzz, SFunction<T, ?> column, Object val) {
        return ne(clzz, true, column, val);
    }

    /**
     * 不等于 &lt;&gt;
     *
     * @param condition 执行条件
     * @param column    字段
     * @param val       值
     * @return children
     */
    <T> Children ne(Class<T> clzz, boolean condition, SFunction<T, ?> column, Object val);

    /**
     * ignore
     */
    default <T> Children gt(Class<T> clzz, SFunction<T, ?> column, Object val) {
        return gt(clzz, true, column, val);
    }

    /**
     * 大于 &gt;
     *
     * @param condition 执行条件
     * @param column    字段
     * @param val       值
     * @return children
     */
    <T> Children gt(Class<T> clzz, boolean condition, SFunction<T, ?> column, Object val);

    /**
     * ignore
     */
    default <T> Children ge(Class<T> clzz, SFunction<T, ?> column, Object val) {
        return ge(clzz, true, column, val);
    }

    /**
     * 大于等于 &gt;=
     *
     * @param condition 执行条件
     * @param column    字段
     * @param val       值
     * @return children
     */
    <T> Children ge(Class<T> clzz, boolean condition, SFunction<T, ?> column, Object val);

    /**
     * ignore
     */
    default <T> Children lt(Class<T> clzz, SFunction<T, ?> column, Object val) {
        return lt(clzz, true, column, val);
    }

    /**
     * 小于 &lt;
     *
     * @param condition 执行条件
     * @param column    字段
     * @param val       值
     * @return children
     */
    <T> Children lt(Class<T> clzz, boolean condition, SFunction<T, ?> column, Object val);

    /**
     * ignore
     */
    default <T> Children le(Class<T> clzz, SFunction<T, ?> column, Object val) {
        return le(clzz, true, column, val);
    }

    /**
     * 小于等于 &lt;=
     *
     * @param condition 执行条件
     * @param column    字段
     * @param val       值
     * @return children
     */
    <T> Children le(Class<T> clzz, boolean condition, SFunction<T, ?> column, Object val);

    /**
     * ignore
     */
    default <T> Children between(Class<T> clzz, SFunction<T, ?> column, Object val1, Object val2) {
        return between(clzz, true, column, val1, val2);
    }

    /**
     * BETWEEN 值1 AND 值2
     *
     * @param condition 执行条件
     * @param column    字段
     * @param val1      值1
     * @param val2      值2
     * @return children
     */
    <T> Children between(Class<T> clzz, boolean condition, SFunction<T, ?> column, Object val1, Object val2);

    /**
     * ignore
     */
    default <T> Children notBetween(Class<T> clzz, SFunction<T, ?> column, Object val1, Object val2) {
        return notBetween(clzz, true, column, val1, val2);
    }

    /**
     * NOT BETWEEN 值1 AND 值2
     *
     * @param condition 执行条件
     * @param column    字段
     * @param val1      值1
     * @param val2      值2
     * @return children
     */
    <T> Children notBetween(Class<T> clzz, boolean condition, SFunction<T, ?> column, Object val1, Object val2);

    /**
     * ignore
     */
    default <T> Children like(Class<T> clzz, SFunction<T, ?> column, Object val) {
        return like(clzz, true, column, val);
    }

    /**
     * LIKE '%值%'
     *
     * @param condition 执行条件
     * @param column    字段
     * @param val       值
     * @return children
     */
    <T> Children like(Class<T> clzz, boolean condition, SFunction<T, ?> column, Object val);

    /**
     * ignore
     */
    default <T> Children notLike(Class<T> clzz, SFunction<T, ?> column, Object val) {
        return notLike(clzz, true, column, val);
    }

    /**
     * NOT LIKE '%值%'
     *
     * @param condition 执行条件
     * @param column    字段
     * @param val       值
     * @return children
     */
    <T> Children notLike(Class<T> clzz, boolean condition, SFunction<T, ?> column, Object val);

    /**
     * ignore
     */
    default <T> Children likeLeft(Class<T> clzz, SFunction<T, ?> column, Object val) {
        return likeLeft(clzz, true, column, val);
    }

    /**
     * LIKE '%值'
     *
     * @param condition 执行条件
     * @param column    字段
     * @param val       值
     * @return children
     */
    <T> Children likeLeft(Class<T> clzz, boolean condition, SFunction<T, ?> column, Object val);

    /**
     * ignore
     */
    default <T> Children likeRight(Class<T> clzz, SFunction<T, ?> column, Object val) {
        return likeRight(clzz, true, column, val);
    }

    /**
     * LIKE '值%'
     *
     * @param condition 执行条件
     * @param column    字段
     * @param val       值
     * @return children
     */
    <T> Children likeRight(Class<T> clzz, boolean condition, SFunction<T, ?> column, Object val);

}
