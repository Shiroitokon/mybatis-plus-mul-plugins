package com.shiroito.mybatis_plus_mul_plugins;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.shiroito.mybatis_plus_mul_plugins.mapper.MultitudeMapper;
import com.shiroito.mybatis_plus_mul_plugins.method.MultitudeOne;
import com.shiroito.mybatis_plus_mul_plugins.method.MultitudePage;
import com.shiroito.mybatis_plus_mul_plugins.method.MultitudeSelect;

import java.lang.reflect.Type;
import java.util.List;

/**
 * @author hongyq (修改代码请联系开发者)
 * @date 2021-04-07 20:49
 */
public class MultitudeSqlInjector  extends DefaultSqlInjector {

    @Override
    public List<AbstractMethod> getMethodList(Class<?> mapperClass) {
        List<AbstractMethod> methodList = super.getMethodList(mapperClass);
        Type[] genericInterfaces = mapperClass.getGenericInterfaces();

        for(Type type : genericInterfaces) {
            if(type.getTypeName().equals(MultitudeMapper.class.getTypeName())) {
                methodList.add(new MultitudeSelect());
                methodList.add(new MultitudePage());
                methodList.add(new MultitudeOne());
                break;
            }
        }

        return methodList;
    }
}
