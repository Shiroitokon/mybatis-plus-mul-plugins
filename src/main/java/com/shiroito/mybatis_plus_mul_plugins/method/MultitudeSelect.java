package com.shiroito.mybatis_plus_mul_plugins.method;


import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.shiroito.mybatis_plus_mul_plugins.AbstractMultitudeMethod;
import com.shiroito.mybatis_plus_mul_plugins.MultitudeContext;
import com.shiroito.mybatis_plus_mul_plugins.PublicMethod;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

/**
 * @author hongyq (修改代码请联系开发者)
 * @date 2021-04-07 20:48
 */
public class MultitudeSelect extends AbstractMultitudeMethod {


    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {


        String method = PublicMethod.MULTITUDE_PAGE;

        SqlMethod sqlMethod = SqlMethod.SELECT_LIST;
        String sql = String.format(sqlMethod.getSql(), sqlFirst(), sqlSelectColumns(),
                sqlMultitudeTable(tableInfo), sqlWhereEntityWrapper(), sqlComment());

        SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, modelClass);

        MultitudeContext.register(mapperClass.getName() + "." + method);

        return addSelectMappedStatementForOther(mapperClass, method, sqlSource, null);

    }

}
