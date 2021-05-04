package com.shiroito.mybatis_plus_mul_plugins.config;

import com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration;
import com.shiroito.mybatis_plus_mul_plugins.MultitudePlugin;
import com.shiroito.mybatis_plus_mul_plugins.MultitudeSqlInjector;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author hongyq (修改代码请联系开发者)
 * @date 2021-04-29 15:14
 */
@Configuration
@AutoConfigureBefore(MybatisPlusAutoConfiguration.class)
public class MulPluginConfig {

    @Bean
    public MultitudeSqlInjector multitudeSqlInjector() {
        return new MultitudeSqlInjector();
    }

    @Bean
    public MultitudePlugin multitudePlugin() {
        return new MultitudePlugin();
    }

}
