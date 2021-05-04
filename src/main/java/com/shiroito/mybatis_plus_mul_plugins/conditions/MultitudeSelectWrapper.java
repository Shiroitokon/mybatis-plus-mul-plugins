package com.shiroito.mybatis_plus_mul_plugins.conditions;

import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.LambdaUtils;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.reflection.property.PropertyNamer;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author hongyq (修改代码请联系开发者)
 * @date 2021-04-09 18:47
 */
public class MultitudeSelectWrapper<T> {

    private Class<T> selectClass;

    private ConcurrentHashMap<String, String> selectMap = new ConcurrentHashMap<>();

    private CopyOnWriteArraySet<String> fileNameCache = new CopyOnWriteArraySet<>();

    private MultitudeTableWrapper<T> multitudeTableWrapper;

    private CopyOnWriteArraySet<String> ignores = new CopyOnWriteArraySet<>();

    /**
     * 冲突字段选择 如果字段冲突 且 未指定 默认选择第一个
     */
    protected ConcurrentHashMap<String, String> conflictSelect = new ConcurrentHashMap<>();



    protected MultitudeSelectWrapper(Class<T> selectClass) {
        this.selectClass = selectClass;
        this.multitudeTableWrapper = new MultitudeTableWrapper<>(this);
        this.init();
    }




    public  <F> MultitudeTableWrapper<T> from(Class<F> fClazz) {

        return multitudeTableWrapper.from(fClazz);
    }

    protected void selectPick(String alis, Class<?> clazz) {

        if(isWrapClass(selectClass)) {
            return;
        }

        TableInfo tableInfo = TableInfoHelper.getTableInfo(clazz);
        selectPut(alis, tableInfo.getKeyProperty(), tableInfo.getKeyColumn());
        List<TableFieldInfo> fieldList = tableInfo.getFieldList();
        for(TableFieldInfo f: fieldList) {
            String column = f.getColumn();
            String name = f.getField().getName();
            selectPut(alis, name, column);
        }
    }



    protected void selectPut(String alis,String name, String column) {
        if(ignores.contains(name)) {
            return;
        }

        if(!fileNameCache.contains(name)) {
            return;
        }

        if(selectMap.containsKey(name)) {
            return;
        }

        String selectName = alis + "." + column;

        if(conflictSelect.containsKey(column)) {

            String conflictSelectName = conflictSelect.get(column);
            if(conflictSelectName.equals(selectName)) {

                selectMap.put(name, selectName);
                return;
            }

            return;
        }
        selectMap.put(name, selectName);
    }

    protected String selectMapToStr() {

        if(selectMap.size() == 0) {
            return "";
        }

        Collection<String> values = selectMap.values();
        String join = StringUtils.join(values, ",");
        return join;
    }

    protected String getSelectSql() {
        return selectMapToStr();
    }


    protected void init() {

        if(isWrapClass(selectClass)) {
            return;
        }
        Field[] declaredFields = selectClass.getDeclaredFields();
        for (Field f: declaredFields) {
            fileNameCache.add(f.getName());
        }
    }

    protected Class<T> getSelectClass() {
        return selectClass;
    }

    public <C> MultitudeSelectWrapper<T> conflictSelect(Class<C> clazz, SFunction<C, ?> sFunction) {
        String alisTable = this.multitudeTableWrapper.tryLoadAndGetAlisTable(clazz);
        String column = AbstractWrapper.getColumn(LambdaUtils.resolve(sFunction), true);
        conflictSelect.put(column, alisTable + "." + column);
        return this;
    }
    

    public <C> MultitudeSelectWrapper<T> count(Class<C> clazz, SFunction<C, ?> sFunction, String alis) {
        String alisTable = this.multitudeTableWrapper.tryLoadAndGetAlisTable(clazz);
        StringBuilder selectSql = new StringBuilder();
        String column = AbstractWrapper.getColumn(LambdaUtils.resolve(sFunction), true);
        selectSql.append(" COUNT(");
        selectSql.append(alisTable);
        selectSql.append(".");
        selectSql.append(column);
        selectSql.append(") ");

        if (alis != null && !alis.equals("")) {
            selectSql.append(" AS ");
            selectSql.append(alis);
        }
        selectMap.put(alis, selectSql.toString());
        return this;
    }

    /**
     * 吃内存
     */
    public <C> MultitudeSelectWrapper<T> count(Class<C> clazz, SFunction<C, ?> sFunction, SFunction<T, ?> tsFunction) {
        String alisTable = this.multitudeTableWrapper.tryLoadAndGetAlisTable(clazz);
        StringBuilder selectSql = new StringBuilder();
        String column = AbstractWrapper.getColumn(LambdaUtils.resolve(sFunction), true);
        String alis = PropertyNamer.methodToProperty(LambdaUtils.resolve(tsFunction).getImplMethodName());
        selectSql.append(" COUNT(");
        selectSql.append(alisTable);
        selectSql.append(".");
        selectSql.append(column);
        selectSql.append(") ");
        selectSql.append(" AS ");
        selectSql.append(alis);
        selectMap.put(alis, selectSql.toString());
        return this;
    }


    public <C> MultitudeSelectWrapper<T> count(Class<C> clazz, SFunction<C, ?> sFunction) {

        return count(clazz, sFunction, "");
    }


    public <C> MultitudeSelectWrapper<T> sum(Class<C> clazz, SFunction<C, ?> sFunction, String alis) {
        String alisTable = this.multitudeTableWrapper.tryLoadAndGetAlisTable(clazz);
        StringBuilder selectSql = new StringBuilder();
        String column = AbstractWrapper.getColumn(LambdaUtils.resolve(sFunction), true);
        selectSql.append(" SUM(");
        selectSql.append(alisTable);
        selectSql.append(".");
        selectSql.append(column);
        selectSql.append(") ");
        if (alis != null && !alis.equals("")) {
            selectSql.append(" AS ");
            selectSql.append(alis);
        }
        selectMap.put(alis, selectSql.toString());
        return this;
    }

    /**
     * 吃内存
     */
    public <C> MultitudeSelectWrapper<T> sum(Class<C> clazz, SFunction<C, ?> sFunction, SFunction<T, ?> tsFunction) {
        String alisTable = this.multitudeTableWrapper.tryLoadAndGetAlisTable(clazz);
        StringBuilder selectSql = new StringBuilder();
        String column = AbstractWrapper.getColumn(LambdaUtils.resolve(sFunction), true);

        String alis = PropertyNamer.methodToProperty(LambdaUtils.resolve(tsFunction).getImplMethodName());

        selectSql.append(" SUM(");
        selectSql.append(alisTable);
        selectSql.append(".");
        selectSql.append(column);
        selectSql.append(") ");
        selectSql.append(" AS ");
        selectSql.append(alis);
        selectMap.put(alis, selectSql.toString());
        return this;
    }


    public <C> MultitudeSelectWrapper<T> sum(Class<C> clazz, SFunction<C, ?> sFunction) {

        return sum(clazz, sFunction, "");
    }

    public MultitudeSelectWrapper<T> ignore(SFunction<T, ?> sFunction) {
        String alis = PropertyNamer.methodToProperty(LambdaUtils.resolve(sFunction).getImplMethodName());
        ignores.add(alis);
        return this;
    }


    public static boolean isWrapClass(Class clz) {
        try {
            return ((Class) clz.getField("TYPE").get(null)).isPrimitive();
        } catch (Exception e) {
            return false;
        }
    }

}
