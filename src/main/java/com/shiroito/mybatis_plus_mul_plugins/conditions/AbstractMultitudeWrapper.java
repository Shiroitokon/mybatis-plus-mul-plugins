package com.shiroito.mybatis_plus_mul_plugins.conditions;

import com.baomidou.mybatisplus.core.toolkit.LambdaUtils;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;

import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.stream.Collectors.joining;

/**
 * @author hongyq (修改代码请联系开发者)
 * @date 2021-04-08 10:38
 */

public abstract class AbstractMultitudeWrapper<Children extends AbstractMultitudeWrapper<Children>> extends AbstractWrapper<Children> {


    private ConcurrentHashMap<Class<?>, String> tableAlis;

    public AbstractMultitudeWrapper(ConcurrentHashMap<Class<?>, String> tableAlis) {
        this.tableAlis = tableAlis;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected <T> String columnsToString(Class<T> clzz, SFunction<T, ?>... columns) {
        return columnsToString(clzz, true, columns);
    }

    @SuppressWarnings("unchecked")
    protected <T> String columnsToString(Class<T> clzz, boolean onlyColumn, SFunction<T, ?>... columns) {
        return Arrays.stream(columns).map(i -> columnToString(clzz, i, onlyColumn)).collect(joining(StringPool.COMMA));
    }

    @Override
    protected <T> String columnToString(Class<T> clzz, SFunction<T, ?> column) {
        return columnToString(clzz, column, true);
    }

    protected <T> String columnToString(Class<T> clzz, SFunction<T, ?> column, boolean onlyColumn) {

        return tableAlis.get(clzz) + "." + getColumn(LambdaUtils.resolve(column), onlyColumn);
    }


    public ConcurrentHashMap<Class<?>, String> getTableAlis() {
        return tableAlis;
    }
}
