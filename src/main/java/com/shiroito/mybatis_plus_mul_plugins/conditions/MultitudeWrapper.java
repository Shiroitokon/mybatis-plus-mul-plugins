package com.shiroito.mybatis_plus_mul_plugins.conditions;

/**
 * @author hongyq (修改代码请联系开发者)
 * @date 2021-04-08 11:45
 */

public class MultitudeWrapper<R> extends AbstractMultitudeWrapper<MultitudeWrapper<R>>{

    private MultitudeSelectWrapper<R> multitudeSelectWrapper;

    private MultitudeTableWrapper<R> multitudeTableWrapper;

    private Class<R> resultMap;

    public MultitudeWrapper(MultitudeSelectWrapper<R> multitudeSelectWrapper, MultitudeTableWrapper<R> multitudeTableWrapper) {
        super(multitudeTableWrapper.getTableAlis());
        this.multitudeSelectWrapper = multitudeSelectWrapper;
        this.multitudeTableWrapper = multitudeTableWrapper;
        this.resultMap = multitudeSelectWrapper.getSelectClass();
        super.initNeed();
    }



    @Override
    protected MultitudeWrapper<R> instance() {
        return new MultitudeWrapper<>(this.multitudeSelectWrapper, this.multitudeTableWrapper);
    }




    public String getSqlSelect() {
        return multitudeSelectWrapper.getSelectSql();
    }

    public String getSqlMultitudeTable() {
        return multitudeTableWrapper.getSqlMultitudeTable();
    }

    public Class<R> getResultMap() {
        return resultMap;
    }
}
