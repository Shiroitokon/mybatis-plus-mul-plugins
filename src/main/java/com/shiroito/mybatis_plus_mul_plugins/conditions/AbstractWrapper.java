package com.shiroito.mybatis_plus_mul_plugins.conditions;

import com.baomidou.mybatisplus.core.conditions.ISqlSegment;
import com.baomidou.mybatisplus.core.conditions.SharedString;
import com.baomidou.mybatisplus.core.conditions.segments.MergeSegments;
import com.baomidou.mybatisplus.core.enums.SqlKeyword;
import com.baomidou.mybatisplus.core.enums.SqlLike;
import com.baomidou.mybatisplus.core.toolkit.*;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlUtils;
import com.baomidou.mybatisplus.core.toolkit.sql.StringEscape;
import com.baomidou.mybatisplus.core.toolkit.support.ColumnCache;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.core.toolkit.support.SerializedLambda;
import org.apache.ibatis.reflection.property.PropertyNamer;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import static com.baomidou.mybatisplus.core.enums.SqlKeyword.*;
import static com.baomidou.mybatisplus.core.enums.WrapperKeyword.APPLY;
import static java.util.stream.Collectors.joining;

/**
 * @author hongyq (修改代码请联系开发者)
 * @date 2021-04-08 14:23
 */
public abstract class AbstractWrapper<Children extends AbstractWrapper<Children>> implements ISqlSegment, Func<Children>, Compare<Children>{

    /**
     * 占位符
     */
    protected final Children typedThis = (Children) this;
    /**
     * 必要度量
     */
    protected AtomicInteger paramNameSeq;
    protected Map<String, Object> paramNameValuePairs;
    protected SharedString lastSql;
    /**
     * SQL注释
     */
    protected SharedString sqlComment;
    /**
     * SQL起始语句
     */
    protected SharedString sqlFirst;
    /**
     * ß
     * 数据库表映射实体类
     */
    protected MergeSegments expression;



    @Override
    public <T> Children eq(Class<T> clzz, boolean condition, SFunction<T, ?> column, Object val) {
        return addCondition(clzz, condition, column, EQ, val);
    }

    @Override
    public <T> Children ne(Class<T> clzz, boolean condition, SFunction<T, ?> column, Object val) {
        return addCondition(clzz, condition, column, NE, val);
    }

    @Override
    public <T> Children gt(Class<T> clzz, boolean condition, SFunction<T, ?> column, Object val) {
        return addCondition(clzz, condition, column, GT, val);
    }

    @Override
    public <T> Children ge(Class<T> clzz, boolean condition, SFunction<T, ?> column, Object val) {
        return addCondition(clzz, condition, column, GE, val);
    }

    @Override
    public <T> Children lt(Class<T> clzz, boolean condition, SFunction<T, ?> column, Object val) {
        return addCondition(clzz, condition, column, LT, val);
    }

    @Override
    public <T> Children le(Class<T> clzz, boolean condition, SFunction<T, ?> column, Object val) {
        return addCondition(clzz, condition, column, LE, val);
    }

    @Override
    public <T> Children like(Class<T> clzz, boolean condition, SFunction<T, ?> column, Object val) {
        return likeValue(clzz, condition, LIKE, column, val, SqlLike.DEFAULT);
    }

    @Override
    public <T> Children notLike(Class<T> clzz, boolean condition, SFunction<T, ?> column, Object val) {
        return likeValue(clzz, condition, NOT_LIKE, column, val, SqlLike.DEFAULT);
    }

    @Override
    public <T> Children likeLeft(Class<T> clzz, boolean condition, SFunction<T, ?> column, Object val) {
        return likeValue(clzz, condition, LIKE, column, val, SqlLike.LEFT);
    }

    @Override
    public <T> Children likeRight(Class<T> clzz, boolean condition, SFunction<T, ?> column, Object val) {
        return likeValue(clzz, condition, LIKE, column, val, SqlLike.RIGHT);
    }

    @Override
    public <T> Children between(Class<T> clzz, boolean condition, SFunction<T, ?> column, Object val1, Object val2) {
        return doIt(condition, () -> columnToString(clzz, column), BETWEEN, () -> formatSql("{0}", val1), AND,
                () -> formatSql("{0}", val2));
    }

    @Override
    public <T> Children notBetween(Class<T> clzz, boolean condition, SFunction<T, ?> column, Object val1, Object val2) {
        return doIt(condition, () -> columnToString(clzz, column), NOT_BETWEEN, () -> formatSql("{0}", val1), AND,
                () -> formatSql("{0}", val2));
    }

    @Override
    public <T> Children isNull(Class<T> clzz, boolean condition, SFunction<T, ?> column) {
        return doIt(condition, () -> columnToString(clzz, column), IS_NULL);
    }
    @Override
    public <T> Children isNotNull(Class<T> clzz, boolean condition, SFunction<T, ?> column) {
        return doIt(condition, () -> columnToString(clzz, column), IS_NOT_NULL);
    }
    @Override
    public <T> Children in(Class<T> clzz, boolean condition, SFunction<T, ?> column, Collection<?> coll) {
        return doIt(condition, () -> columnToString(clzz, column), IN, inExpression(coll));
    }
    @Override
    public <T> Children notIn(Class<T> clzz, boolean condition, SFunction<T, ?> column, Collection<?> coll) {
        return doIt(condition, () -> columnToString(clzz, column), NOT_IN, inExpression(coll));
    }
    @Override
    public <T> Children inSql(Class<T> clzz, boolean condition, SFunction<T, ?> column, String inValue) {
        return doIt(condition, () -> columnToString(clzz, column), IN, () -> String.format("(%s)", inValue));
    }
    @Override
    public <T> Children notInSql(Class<T> clzz, boolean condition, SFunction<T, ?> column, String inValue) {
        return doIt(condition, () -> columnToString(clzz, column), NOT_IN, () -> String.format("(%s)", inValue));
    }
    @Override
    public <T> Children groupBy(Class<T> clzz, boolean condition, SFunction<T, ?> ... columns) {
        if (ArrayUtils.isEmpty(columns)) {
            return typedThis;
        }
        return doIt(condition, GROUP_BY,
                () -> columns.length == 1 ? columnToString(clzz, columns[0]) : columnsToString(clzz, columns));
    }
    @Override
    public <T> Children orderBy(Class<T> clzz, boolean condition, boolean isAsc, SFunction<T, ?> ... columns) {
        if (ArrayUtils.isEmpty(columns)) {
            return typedThis;
        }
        SqlKeyword mode = isAsc ? ASC : DESC;
        for (SFunction<T, ?> column : columns) {
            doIt(condition, ORDER_BY, () -> columnToString(clzz, column), mode);
        }
        return typedThis;
    }


    public Children and(boolean condition, Consumer<Children> consumer) {
        return and(condition).addNestedCondition(condition, consumer);
    }

    public Children or(boolean condition, Consumer<Children> consumer) {
        return or(condition).addNestedCondition(condition, consumer);
    }

    public Children nested(boolean condition, Consumer<Children> consumer) {
        return addNestedCondition(condition, consumer);
    }

    public Children not(boolean condition, Consumer<Children> consumer) {
        return not(condition).addNestedCondition(condition, consumer);
    }

    public Children or(boolean condition) {
        return doIt(condition, OR);
    }


    public Children apply(boolean condition, String applySql, Object... value) {
        return doIt(condition, APPLY, () -> formatSql(applySql, value));
    }

    public Children last(boolean condition, String lastSql) {
        if (condition) {
            this.lastSql.setStringValue(StringPool.SPACE + lastSql);
        }
        return typedThis;
    }


    public Children last(String lastSql) {
        last(true, lastSql);
        return typedThis;
    }


    public Children one() {
        last("limit 1");
        return typedThis;
    }

    public Children comment(boolean condition, String comment) {
        if (condition) {
            this.sqlComment.setStringValue(comment);
        }
        return typedThis;
    }



    public Children exists(boolean condition, String existsSql) {
        return doIt(condition, EXISTS, () -> String.format("(%s)", existsSql));
    }

    public Children notExists(boolean condition, String existsSql) {
        return not(condition).exists(condition, existsSql);
    }

    @Override
    public Children having(boolean condition, String sqlHaving, Object... params) {
        return doIt(condition, HAVING, () -> formatSqlIfNeed(condition, sqlHaving, params));
    }
    @Override
    public Children func(boolean condition, Consumer<Children> consumer) {
        if (condition) {
            consumer.accept(typedThis);
        }
        return typedThis;
    }

    /**
     * 内部自用
     * <p>NOT 关键词</p>
     */
    protected Children not(boolean condition) {
        return doIt(condition, NOT);
    }

    /**
     * 内部自用
     * <p>拼接 AND</p>
     */
    protected Children and(boolean condition) {
        return doIt(condition, AND);
    }

    /**
     * 内部自用
     * <p>拼接 LIKE 以及 值</p>
     */
    protected <T> Children likeValue(Class<T> clzz, boolean condition, SqlKeyword keyword, SFunction<T, ?> column, Object val, SqlLike sqlLike) {
        return doIt(condition, () -> columnToString(clzz, column), keyword, () -> formatSql("{0}", SqlUtils.concatLike(val, sqlLike)));
    }

    /**
     * 普通查询条件
     *
     * @param condition  是否执行
     * @param column     属性
     * @param sqlKeyword SQL 关键词
     * @param val        条件值
     */
    protected <T> Children addCondition(Class<T> clzz, boolean condition, SFunction<T, ?> column, SqlKeyword sqlKeyword, Object val) {
        return doIt(condition, () -> columnToString(clzz, column), sqlKeyword, () -> formatSql("{0}", val));
    }

    /**
     * 多重嵌套查询条件
     *
     * @param condition 查询条件值
     */
    protected Children addNestedCondition(boolean condition, Consumer<Children> consumer) {
        if (condition) {
            final Children instance = instance();
            consumer.accept(instance);
            return doIt(true, APPLY, instance);
        }
        return typedThis;
    }

    /**
     * 子类返回一个自己的新对象
     */
    protected abstract Children instance();

    /**
     * 格式化SQL
     *
     * @param sqlStr SQL语句部分
     * @param params 参数集
     * @return sql
     */
    protected final String formatSql(String sqlStr, Object... params) {
        return formatSqlIfNeed(true, sqlStr, params);
    }

    /**
     * <p>
     * 根据需要格式化SQL<br>
     * <br>
     * Format SQL for methods: EntityQ<T>.where/and/or...("name={0}", value);
     * ALL the {<b>i</b>} will be replaced with #{MPGENVAL<b>i</b>}<br>
     * <br>
     * ew.where("sample_name=<b>{0}</b>", "haha").and("sample_age &gt;<b>{0}</b>
     * and sample_age&lt;<b>{1}</b>", 18, 30) <b>TO</b>
     * sample_name=<b>#{MPGENVAL1}</b> and sample_age&gt;#<b>{MPGENVAL2}</b> and
     * sample_age&lt;<b>#{MPGENVAL3}</b><br>
     * </p>
     *
     * @param need   是否需要格式化
     * @param sqlStr SQL语句部分
     * @param params 参数集
     * @return sql
     */
    protected final String formatSqlIfNeed(boolean need, String sqlStr, Object... params) {
        if (!need || StringUtils.isBlank(sqlStr)) {
            return null;
        }
        if (ArrayUtils.isNotEmpty(params)) {
            for (int i = 0; i < params.length; ++i) {
                String genParamName = Constants.WRAPPER_PARAM + paramNameSeq.incrementAndGet();
                sqlStr = sqlStr.replace(String.format("{%s}", i),
                        String.format(Constants.WRAPPER_PARAM_FORMAT, Constants.WRAPPER, genParamName));
                paramNameValuePairs.put(genParamName, params[i]);
            }
        }
        return sqlStr;
    }

    /**
     * 获取in表达式 包含括号
     *
     * @param value 集合
     */
    private ISqlSegment inExpression(Collection<?> value) {
        return () -> value.stream().map(i -> formatSql("{0}", i))
                .collect(joining(StringPool.COMMA, StringPool.LEFT_BRACKET, StringPool.RIGHT_BRACKET));
    }

    /**
     * 必要的初始化
     */
    protected void initNeed() {
        paramNameSeq = new AtomicInteger(0);
        paramNameValuePairs = new HashMap<>(16);
        expression = new MergeSegments();
        lastSql = SharedString.emptyString();
        sqlComment = SharedString.emptyString();
        sqlFirst = SharedString.emptyString();
    }

    public void clear() {
        paramNameSeq.set(0);
        paramNameValuePairs.clear();
        expression.clear();
        lastSql.toEmpty();
        sqlComment.toEmpty();
        sqlFirst.toEmpty();
    }

    /**
     * 对sql片段进行组装
     *
     * @param condition   是否执行
     * @param sqlSegments sql片段数组
     * @return children
     */
    protected Children doIt(boolean condition, ISqlSegment... sqlSegments) {
        if (condition) {
            expression.add(sqlSegments);
        }
        return typedThis;
    }

    @Override
    public String getSqlSegment() {
        return expression.getSqlSegment();
    }


    public String getLastSql() {
        return lastSql.getStringValue();
    }

    public String getSqlComment() {
        if (StringUtils.isNotBlank(sqlComment.getStringValue())) {
            return "/*" + StringEscape.escapeRawString(sqlComment.getStringValue()) + "*/";
        }
        return null;
    }

    public String getSqlFirst() {
        if (StringUtils.isNotBlank(sqlFirst.getStringValue())) {
            return StringEscape.escapeRawString(sqlFirst.getStringValue());
        }
        return null;
    }

    public MergeSegments getExpression() {
        return expression;
    }

    public Map<String, Object> getParamNameValuePairs() {
        return paramNameValuePairs;
    }

    /**
     * 获取 columnName
     */
    protected abstract <T> String columnToString(Class<T> clzz, SFunction<T, ?> column);

    /**
     * 多字段转换为逗号 "," 分割字符串
     *
     * @param columns 多字段
     */
    protected abstract  <T> String columnsToString(Class<T> clzz, SFunction<T, ?>... columns);

    @Override
    @SuppressWarnings("all")
    public Children clone() {
        return SerializationUtils.clone(typedThis);
    }



    /**
     * 获取 SerializedLambda 对应的列信息，从 lambda 表达式中推测实体类
     * <p>
     * 如果获取不到列信息，那么本次条件组装将会失败
     *
     * @param lambda     lambda 表达式
     * @param onlyColumn 如果是，结果: "name", 如果否： "name" as "name"
     * @return 列
     * @throws com.baomidou.mybatisplus.core.exceptions.MybatisPlusException 获取不到列信息时抛出异常
     * @see SerializedLambda#getImplClass()
     * @see SerializedLambda#getImplMethodName()
     */
    protected static String getColumn(SerializedLambda lambda, boolean onlyColumn) {
        Class<?> aClass = lambda.getInstantiatedType();
        Map<String, ColumnCache> columnMap  = LambdaUtils.getColumnMap(aClass);
        String fieldName = PropertyNamer.methodToProperty(lambda.getImplMethodName());
        ColumnCache columnCache = columnMap.get(LambdaUtils.formatKey(fieldName));;
        return onlyColumn ? columnCache.getColumn() : columnCache.getColumnSelect();
    }


    /**
     * 查询条件为空
     */
    public boolean emptyOfWhere() {
        return CollectionUtils.isEmpty(expression.getNormal());
    }
}
