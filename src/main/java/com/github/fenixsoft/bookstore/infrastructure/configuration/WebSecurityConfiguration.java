package com.github.fenixsoft.bookstore.infrastructure.configuration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * Spring Security安全配置
 * <p>
 * 移除静态资源目录的安全控制，避免Spring Security默认禁止HTTP缓存的行为
 *
 * @author icyfenix@gmail.com
 * @date 2020/4/8 0:09
 **/
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.headers().cacheControl().disable();
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/static/**");
    }
}
