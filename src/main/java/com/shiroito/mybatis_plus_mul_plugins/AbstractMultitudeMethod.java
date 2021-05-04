package com.shiroito.mybatis_plus_mul_plugins;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlScriptUtils;

/**
 * @author hongyq (修改代码请联系开发者)
 * @date 2021-04-12 12:09
 */
public abstract class AbstractMultitudeMethod extends AbstractMethod {


    protected String sqlWhereEntityWrapper() {
        String sqlScript = SqlScriptUtils.convertIf(
                SqlScriptUtils.convertWhere(String.format(" ${%s}", WRAPPER_SQLSEGMENT))
                ,
                String.format("%s != null and %s != '' and !%s", WRAPPER_SQLSEGMENT, WRAPPER_SQLSEGMENT, WRAPPER_EMPTYOFWHERE), true);
        sqlScript = SqlScriptUtils.convertIf(sqlScript, String.format("%s != null", WRAPPER), true);


        sqlScript += SqlScriptUtils.convertIf(SqlScriptUtils.convertIf(String.format(" ${%s}", WRAPPER_SQLSEGMENT), String.format("%s != null and %s != '' and %s", WRAPPER_SQLSEGMENT, WRAPPER_SQLSEGMENT, WRAPPER_EMPTYOFWHERE), true), String.format("%s != null", WRAPPER), true);

        sqlScript += SqlScriptUtils.convertIf(String.format(" ${%s}", WRAPPER + ".lastSql"), String.format("%s != null and %s !='' ", WRAPPER + ".lastSql", WRAPPER + ".lastSql"), true);

        return  NEWLINE + sqlScript;
    }

    protected String sqlSelectColumns() {
        return SqlScriptUtils.convertChoose(String.format("%s != null and %s != null", WRAPPER, Q_WRAPPER_SQL_SELECT),
                SqlScriptUtils.unSafeParam(Q_WRAPPER_SQL_SELECT), ASTERISK);
    }

    protected String sqlMultitudeTable(TableInfo tableInfo) {
        return SqlScriptUtils.convertChoose(String.format("%s != null and %s != null", WRAPPER, WRAPPER + ".sqlMultitudeTable"),
                SqlScriptUtils.unSafeParam(WRAPPER + ".sqlMultitudeTable"), tableInfo.getTableName());
    }


}
