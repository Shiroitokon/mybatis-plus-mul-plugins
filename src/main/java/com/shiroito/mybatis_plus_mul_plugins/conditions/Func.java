package com.shiroito.mybatis_plus_mul_plugins.conditions;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.function.Consumer;

import static java.util.stream.Collectors.toList;

/**
 * @author hongyq (修改代码请联系开发者)
 * @date 2021-04-23 15:13
 */
public interface Func<Children> extends Serializable {


    /**
     * ignore
     */
    default <T> Children isNull(Class<T> clzz, SFunction<T, ?> column) {
        return isNull(clzz, true, column);
    }

    /**
     * 字段 IS NULL
     * <p>例: isNull("name")</p>
     *
     * @param condition 执行条件
     * @param column    字段
     * @return children
     */
    <T> Children isNull(Class<T> clzz, boolean condition, SFunction<T, ?> column);

    /**
     * ignore
     */
    default <T> Children isNotNull(Class<T> clzz, SFunction<T, ?> column) {
        return isNotNull(clzz, true, column);
    }

    /**
     * 字段 IS NOT NULL
     * <p>例: isNotNull("name")</p>
     *
     * @param condition 执行条件
     * @param column    字段
     * @return children
     */
    <T> Children isNotNull(Class<T> clzz, boolean condition, SFunction<T, ?> column);

    /**
     * ignore
     */
    default <T> Children in(Class<T> clzz, SFunction<T, ?> column, Collection<?> coll) {
        return in(clzz, true, column, coll);
    }

    /**
     * 字段 IN (value.get(0), value.get(1), ...)
     * <p>例: in("id", Arrays.asList(1, 2, 3, 4, 5))</p>
     *
     * <li> 如果集合为 empty 则不会进行 sql 拼接 </li>
     *
     * @param condition 执行条件
     * @param column    字段
     * @param coll      数据集合
     * @return children
     */
    <T> Children in(Class<T> clzz, boolean condition, SFunction<T, ?> column, Collection<?> coll);

    /**
     * ignore
     */
    default <T> Children in(Class<T> clzz, SFunction<T, ?> column, Object... values) {
        return in(clzz, true, column, values);
    }

    /**
     * 字段 IN (v0, v1, ...)
     * <p>例: in("id", 1, 2, 3, 4, 5)</p>
     *
     * <li> 如果动态数组为 empty 则不会进行 sql 拼接 </li>
     *
     * @param condition 执行条件
     * @param column    字段
     * @param values    数据数组
     * @return children
     */
    default <T> Children in(Class<T> clzz, boolean condition, SFunction<T, ?> column, Object... values) {
        return in(clzz, condition, column, Arrays.stream(Optional.ofNullable(values).orElseGet(() -> new Object[]{}))
                .collect(toList()));
    }

    /**
     * ignore
     */
    default <T> Children notIn(Class<T> clzz, SFunction<T, ?> column, Collection<?> coll) {
        return notIn(clzz, true, column, coll);
    }

    /**
     * 字段 NOT IN (value.get(0), value.get(1), ...)
     * <p>例: notIn("id", Arrays.asList(1, 2, 3, 4, 5))</p>
     *
     * @param condition 执行条件
     * @param column    字段
     * @param coll      数据集合
     * @return children
     */
    <T> Children notIn(Class<T> clzz, boolean condition, SFunction<T, ?> column, Collection<?> coll);

    /**
     * ignore
     */
    default <T> Children notIn(Class<T> clzz, SFunction<T, ?> column, Object... value) {
        return notIn(clzz, true, column, value);
    }

    /**
     * 字段 NOT IN (v0, v1, ...)
     * <p>例: notIn("id", 1, 2, 3, 4, 5)</p>
     *
     * @param condition 执行条件
     * @param column    字段
     * @param values    数据数组
     * @return children
     */
    default <T> Children notIn(Class<T> clzz, boolean condition, SFunction<T, ?> column, Object... values) {
        return notIn(clzz, condition, column, Arrays.stream(Optional.ofNullable(values).orElseGet(() -> new Object[]{}))
                .collect(toList()));
    }

    /**
     * ignore
     */
    default <T> Children inSql(Class<T> clzz, SFunction<T, ?> column, String inValue) {
        return inSql(clzz, true, column, inValue);
    }

    /**
     * 字段 IN ( sql语句 )
     * <p>!! sql 注入方式的 in 方法 !!</p>
     * <p>例1: inSql("id", "1, 2, 3, 4, 5, 6")</p>
     * <p>例2: inSql("id", "select id from table where id &lt; 3")</p>
     *
     * @param condition 执行条件
     * @param column    字段
     * @param inValue   sql语句
     * @return children
     */
    <T> Children inSql(Class<T> clzz, boolean condition, SFunction<T, ?> column, String inValue);

    /**
     * ignore
     */
    default <T> Children notInSql(Class<T> clzz, SFunction<T, ?> column, String inValue) {
        return notInSql(clzz, true, column, inValue);
    }

    /**
     * 字段 NOT IN ( sql语句 )
     * <p>!! sql 注入方式的 not in 方法 !!</p>
     * <p>例1: notInSql("id", "1, 2, 3, 4, 5, 6")</p>
     * <p>例2: notInSql("id", "select id from table where id &lt; 3")</p>
     *
     * @param condition 执行条件
     * @param column    字段
     * @param inValue   sql语句 ---&gt; 1,2,3,4,5,6 或者 select id from table where id &lt; 3
     * @return children
     */
    <T> Children notInSql(Class<T> clzz, boolean condition, SFunction<T, ?> column, String inValue);

    /**
     * ignore
     */
    default <T> Children groupBy(Class<T> clzz, SFunction<T, ?> column) {
        return groupBy(clzz, true, column);
    }

    /**
     * ignore
     */
    default <T> Children groupBy(Class<T> clzz, SFunction<T, ?>... columns) {
        return groupBy(clzz, true, columns);
    }

    /**
     * 分组：GROUP BY 字段, ...
     * <p>例: groupBy("id", "name")</p>
     *
     * @param condition 执行条件
     * @param columns   字段数组
     * @return children
     */
    <T> Children groupBy(Class<T> clzz, boolean condition, SFunction<T, ?>... columns);

    /**
     * ignore
     */
    default <T> Children orderByAsc(Class<T> clzz, SFunction<T, ?> column) {
        return orderByAsc(clzz, true, column);
    }

    /**
     * ignore
     */
    default <T> Children orderByAsc(Class<T> clzz, SFunction<T, ?>... columns) {
        return orderByAsc(clzz, true, columns);
    }

    /**
     * 排序：ORDER BY 字段, ... ASC
     * <p>例: orderByAsc("id", "name")</p>
     *
     * @param condition 执行条件
     * @param columns   字段数组
     * @return children
     */
    default <T> Children orderByAsc(Class<T> clzz, boolean condition, SFunction<T, ?>... columns) {
        return orderBy(clzz, condition, true, columns);
    }

    /**
     * ignore
     */
    default <T> Children orderByDesc(Class<T> clzz, SFunction<T, ?> column) {
        return orderByDesc(clzz, true, column);
    }

    /**
     * ignore
     */
    default <T> Children orderByDesc(Class<T> clzz, SFunction<T, ?>... columns) {
        return orderByDesc(clzz, true, columns);
    }

    /**
     * 排序：ORDER BY 字段, ... DESC
     * <p>例: orderByDesc("id", "name")</p>
     *
     * @param condition 执行条件
     * @param columns   字段数组
     * @return children
     */
    default <T> Children orderByDesc(Class<T> clzz, boolean condition, SFunction<T, ?>... columns) {
        return orderBy(clzz, condition, false, columns);
    }

    /**
     * 排序：ORDER BY 字段, ...
     * <p>例: orderBy(true, "id", "name")</p>
     *
     * @param condition 执行条件
     * @param isAsc     是否是 ASC 排序
     * @param columns   字段数组
     * @return children
     */
    <T> Children orderBy(Class<T> clzz, boolean condition, boolean isAsc, SFunction<T, ?>... columns);

    /**
     * ignore
     */
    default Children having(String sqlHaving, Object... params) {
        return having(true, sqlHaving, params);
    }

    /**
     * HAVING ( sql语句 )
     * <p>例1: having("sum(age) &gt; 10")</p>
     * <p>例2: having("sum(age) &gt; {0}", 10)</p>
     *
     * @param condition 执行条件
     * @param sqlHaving sql 语句
     * @param params    参数数组
     * @return children
     */
    Children having(boolean condition, String sqlHaving, Object... params);

    /**
     * ignore
     */
    default Children func(Consumer<Children> consumer) {
        return func(true, consumer);
    }

    /**
     * 消费函数
     *
     * @param consumer 消费函数
     * @return children
     * @since 3.3.1
     */
    Children func(boolean condition, Consumer<Children> consumer);
}
