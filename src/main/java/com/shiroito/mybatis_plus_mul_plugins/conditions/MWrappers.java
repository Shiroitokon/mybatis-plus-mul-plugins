package com.shiroito.mybatis_plus_mul_plugins.conditions;

/**
 * @author hongyq (修改代码请联系开发者)
 * @date 2021-04-08 11:45
 */

public class MWrappers{


    public static <R> MultitudeSelectWrapper<R> select(Class<R> returnMap) {

        return new MultitudeSelectWrapper<>(returnMap);
    }

}
