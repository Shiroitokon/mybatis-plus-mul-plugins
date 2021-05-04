package com.shiroito.mybatis_plus_mul_plugins.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shiroito.mybatis_plus_mul_plugins.conditions.MultitudeWrapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * mybatis-plus增强 继承mapper实现 简单 左连接 右链接 内连接查询
 * @author hongyq (修改代码请联系开发者)
 * @date 2021-04-07 20:32
 */
public interface MultitudeMapper {

   <T> List<T> multitudeSelect(@Param(Constants.WRAPPER) MultitudeWrapper<T> queryWrapper);


   <T> IPage<T> multitudePage(Page page, @Param(Constants.WRAPPER) MultitudeWrapper<T> queryWrapper);


   <T> T multitudeOne(@Param(Constants.WRAPPER) MultitudeWrapper<T> queryWrapper);

}
