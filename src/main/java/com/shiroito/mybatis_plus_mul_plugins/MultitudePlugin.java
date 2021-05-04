package com.shiroito.mybatis_plus_mul_plugins;

import com.shiroito.mybatis_plus_mul_plugins.conditions.MultitudeWrapper;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author hongyq (修改代码请联系开发者)
 * @date 2021-04-09 10:17
 */
@Intercepts({
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class}) })
public class MultitudePlugin implements Interceptor {
    @Override
    public Object intercept(Invocation invocation) throws Throwable {

            Object[] queryArgs = invocation.getArgs();
            MappedStatement mappedStatement = (MappedStatement) queryArgs[0];

            boolean contains = MultitudeContext.contains(mappedStatement.getId());
            if(!contains) {

                return invocation.proceed();
            }

        Object paramMap = queryArgs[1];

         if(!(paramMap instanceof Map)) {

             return invocation.proceed();

         }
         List<ResultMap> resultMaps = mappedStatement.getResultMaps();
         if(resultMaps != null && resultMaps.size() != 0) {
             return invocation.proceed();
         }

        Object o = ((Map) paramMap).get("ew");
         if(!(o instanceof MultitudeWrapper)) {
             return invocation.proceed();
         }
         Class resultMap = ((MultitudeWrapper) o).getResultMap();

         if(resultMap == null) {
             return invocation.proceed();
         }

         MappedStatement newMs = copyFromMappedStatement(mappedStatement, resultMap);
         queryArgs[0] = newMs;
         Object result = invocation.proceed();
         return result;

    }



    // 复制原始MappedStatement
    private MappedStatement copyFromMappedStatement(MappedStatement ms, Class<?> resultMap) {
        MappedStatement.Builder builder = new MappedStatement.Builder(ms.getConfiguration(), ms.getId(), ms.getSqlSource(),
                ms.getSqlCommandType());
        builder.resource(ms.getResource());
        builder.fetchSize(ms.getFetchSize());
        builder.statementType(ms.getStatementType());
        builder.keyGenerator(ms.getKeyGenerator());
        if (ms.getKeyProperties() != null) {
            for (String keyProperty : ms.getKeyProperties()) {
                builder.keyProperty(keyProperty);
            }
        }
        builder.timeout(ms.getTimeout());
        builder.parameterMap(ms.getParameterMap());

        //重设resultMap
        Configuration configuration = ms.getConfiguration();
        ResultMap build = new ResultMap.Builder(configuration, ms.getId(), resultMap, new ArrayList<>()).build();
        ArrayList<ResultMap> resultMaps = new ArrayList<>();
        resultMaps.add(build);
        builder.resultMaps(resultMaps);
        builder.cache(ms.getCache());
        builder.useCache(ms.isUseCache());
        return builder.build();
    }




}
