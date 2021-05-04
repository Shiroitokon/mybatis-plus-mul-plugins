package com.shiroito.mybatis_plus_mul_plugins;

import java.util.HashSet;

/**
 * @author hongyq (修改代码请联系开发者)
 * @date 2021-04-09 15:16
 */
public class MultitudeContext {

    private static HashSet<String> multitudeMethods = new HashSet<>();



    public static void register(String id) {
        multitudeMethods.add(id);
    }


    public static boolean contains(String id) {
        return multitudeMethods.contains(id);
    }
}
