# mybatis-plus-mul-plugins

支持mybatis-plus无XML的多表联查、左连接、右链接、内连接以及动态生成resulMap和根据resultMap动态选择select字段
___
## install
### 方式一
1. mvn install  
2. 复制target目录下的jar引入项目中
### 方式二
1. 将项目mvn deploy 进入私服中~~已经上传~~（暂时没有上传到maven公共库）
2. 在pom文件中引入
```
        <dependency>
            <groupId>com.shiroito</groupId>
            <artifactId>mybatis-plus-mul-plugins</artifactId>
            <version>1.0.1</version>
        </dependency>
```
___
## use
**项目中必须先引入mybatis-plus相关依赖**  
对需要增强的mapper接口 继承 MultitudeMapper类
```
@Mapper
public interface CopyInfoMapper extends BaseMapper<CopyInfo>, MultitudeMapper {
}
```
在 MultitudeMapper 中提供了3个常用接口增强  
```
 <T> List<T> multitudeSelect(@Param("ew") MultitudeWrapper<T> var1); //多表查询返回列表
 <T> IPage<T> multitudePage(Page var1, @Param("ew") MultitudeWrapper<T> var2); //多表查询返回page
 <T> T multitudeOne(@Param("ew") MultitudeWrapper<T> var1); //多表查询返回实体
 ```
### demo
1. 常用select查询
```
        //CopyInfo.class, CopyDownloadRecord.class 为表映射实体

        List<CopyInfoVO> copyInfoVOS = copyInfoMapper.multitudeSelect(MWrappers
                .select(CopyInfoVO.class) //resultMap的实体 ，同时也会根据实体内容动态生成select列
                .from(CopyInfoVO.class) //查询的表
                .leftJoin(CopyDownloadRecord.class) //左连接的表
                .on(CopyInfo.class, CopyInfo::getCopyNo, CopyDownloadRecord::getCopyNo) //相当于 left join on
                .where() // where之后 具体使用跟 mybatis-plus 无区别，但需要在第一个入参中指定查询的表实体
                .lt(CopyDownloadRecord.class, CopyDownloadRecord::getCreateTime, new Date())); 

    /**
     *  对应的伪sql为
     *  select xx, xx from copy_info t0 left join copy_download_record t1 on t0.copy_no = t1.copy_no where t1.create_time > now() 
     */

``` 
2. 聚合查询
```     
        //查询拷贝号为123456的拷贝数量
        Long count = copyInfoMapper.multitudeOne(MWrappers
                .select(Long.class) //指定resultMap 为Long.class
                .count(CopyInfo.class, CopyInfo::getCopyNo) //相当于 COUNT(copy_no) 同时支持原生的sql函数调用
                .from(CopyInfo.class)
                .leftJoin(CopyDownloadRecord.class)
                .on(CopyInfo.class, CopyInfo::getCopyNo, CopyDownloadRecord::getCopyNo)
                .where()
                .lt(CopyDownloadRecord.class, CopyDownloadRecord::getCreateTime, new Date())
                .eq(CopyInfo.class, CopyInfo::getCopyNo, "123456")
        );

```
3. resultMap字段冲突
```
        //CopyInfo 和 CopyDownloadRecord 都存在create_time字段，默认情况下选择器使用优先匹配原则（选择第一个匹配上字段的表）
        //我们可以使用conflictSelect 去使字段强制选择指定表中的字段
        IPage<CopyInfoVO> copyInfoVOIPage = copyInfoMapper.multitudePage(new Page(1, 10), MWrappers
                .select(CopyInfoVO.class)
                .conflictSelect(CopyDownloadRecord.class, CopyDownloadRecord::getCreateTime) //强制选择CopyDownloadRecord的create_time
                .from(CopyInfo.class)
                .leftJoin(CopyDownloadRecord.class)
                .on(CopyInfo.class, CopyInfo::getCopyNo, CopyDownloadRecord::getCopyNo)
                .where()
                .lt(CopyDownloadRecord.class, CopyDownloadRecord::getCreateTime, new Date())
                .eq(CopyInfo.class, CopyInfo::getCopyNo, "123"));
```

# 总结
纵观各大orm不管是轻量的还是重量的都在往去sql化发展，让程序能够更加专注于代码，而表和实体的一对一映射对于程序而言有更强的逻辑表达，对于未来的分库分表也能够更加友好的兼容，mybatis-plus的设计压根就不考虑多表链接的情况~~需要去写sql和xml配置~~，这种做法其实是正确的，也是程序开发的方向（单表独立查询，在程序内存中做匹配），这样做一方面能够最大限度的利用索引的优势，另一方面能够跨平台的兼容程序，还有就是在未来分库分表不需要做大规模的重构，尽管这样傻逼的需求总是无穷无尽的，而且甚至有时候会觉得，那么的傻逼又是如此的合理，程序最终还是为需求服务的，大量的左连接，右链接无法淘汰，只能硬着头皮冲~~等以后卡得不行再改回来~~

## License

[MIT](LICENSE) © Shiroitokon



