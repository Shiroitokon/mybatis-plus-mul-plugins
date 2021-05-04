package com.shiroito.mybatis_plus_mul_plugins.conditions;

import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.LambdaUtils;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static com.shiroito.mybatis_plus_mul_plugins.conditions.AbstractWrapper.getColumn;


/**
 * @author hongyq (修改代码请联系开发者)
 * @date 2021-04-09 18:49
 */
public class MultitudeTableWrapper<T> {

    private MultitudeSelectWrapper<T> multitudeSelectWrapper;

    private ConcurrentHashMap<Class<?>, String> tableAlis = new ConcurrentHashMap<>();

    private AtomicInteger alisSeq = new AtomicInteger(0);

    private StringBuffer stringBuffer = new StringBuffer();

    protected  <F> MultitudeTableWrapper(MultitudeSelectWrapper<T> multitudeSelectWrapper) {
        this.multitudeSelectWrapper = multitudeSelectWrapper;
    }


    protected <F> MultitudeTableWrapper<T> from(Class<F> fClazz) {
        String alisTable = tryLoadAndGetAlisTable(fClazz);
        initFrom(fClazz, alisTable);
        return this;
    }

    private void initFrom(Class<?> clazz, String alis) {
        TableInfo tableInfo = TableInfoHelper.getTableInfo(clazz);
        String tableName = tableInfo.getTableName();
        stringBuffer.append(tableName + " " + alis);
    }



    public <LJ> OnWrapper<LJ, T> leftJoin(Class<LJ> clzz) {
        String alisTable = tryLoadAndGetAlisTable(clzz);

        stringBuffer.append(" LEFT JOIN ");
        TableInfo tableInfo = TableInfoHelper.getTableInfo(clzz);
        String tableName = tableInfo.getTableName();
        stringBuffer.append(tableName + " " + alisTable);
        return new OnWrapper<>(this, clzz);

    }




    public <RJ> OnWrapper<RJ, T> rightJoin(Class<RJ> clzz) {
        String alisTable = tryLoadAndGetAlisTable(clzz);
        stringBuffer.append(" RIGHT JOIN ");
        TableInfo tableInfo = TableInfoHelper.getTableInfo(clzz);
        String tableName = tableInfo.getTableName();
        stringBuffer.append(tableName + " " + alisTable);
        return new OnWrapper<>(this, clzz);

    }


    public <NJ> OnWrapper<NJ, T> innerJoin(Class<NJ> clzz) {
        String alisTable = tryLoadAndGetAlisTable(clzz);
        stringBuffer.append(" INNER JOIN ");
        TableInfo tableInfo = TableInfoHelper.getTableInfo(clzz);
        String tableName = tableInfo.getTableName();
        stringBuffer.append(tableName + " " + alisTable);
        return new OnWrapper<>(this, clzz);

    }


    public MultitudeWrapper<T> where() {

        return new MultitudeWrapper<T>(multitudeSelectWrapper, this);
    }


    protected String tryLoadAndGetAlisTable(Class<?> clazz) {

        String alis = this.tableAlis.get(clazz);

        if(alis == null) {
            this.tableAlis.putIfAbsent(clazz, "t" + this.alisSeq.addAndGet(1));
            alis = this.tableAlis.get(clazz);
            this.multitudeSelectWrapper.selectPick(alis, clazz);
        }

        return alis;

    }



    public static class OnWrapper<J, MT>{

        private MultitudeTableWrapper<MT> multitudeTableWrapper;

        private Class<J> formClass;

        public OnWrapper(MultitudeTableWrapper<MT> multitudeTableWrapper, Class<J> formClass) {
            this.multitudeTableWrapper = multitudeTableWrapper;
            this.formClass = formClass;
        }

        public <T> MultitudeTableWrapper<MT> on(Class<T> clazz, SFunction<T, ?> sFunction1 , SFunction<J, ?> sFunction2) {


            StringBuffer stringBuffer = multitudeTableWrapper.getStringBuffer();
            stringBuffer.append(" ON " );
            String t = getColumn(LambdaUtils.resolve(sFunction1), true);
            String j = getColumn(LambdaUtils.resolve(sFunction2), true);

            String tp = multitudeTableWrapper.tryLoadAndGetAlisTable(clazz);
            String tj = multitudeTableWrapper.tryLoadAndGetAlisTable(formClass);

            stringBuffer.append(tp + "." + t);
            stringBuffer.append("=");
            stringBuffer.append(tj + "." + j);
            return multitudeTableWrapper;
        }

    }

    protected StringBuffer getStringBuffer() {
        return stringBuffer;
    }

    protected ConcurrentHashMap<Class<?>, String> getTableAlis() {
        return tableAlis;
    }

    protected String getSqlMultitudeTable() {
        return stringBuffer.toString();
    }
}
