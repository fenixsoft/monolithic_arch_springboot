/*
 * Copyright 2012-2020. the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. More information from:
 *
 *        https://github.com/fenixsoft
 */

package com.github.fenixsoft.bookstore.infrastructure.configuration;

import com.github.fenixsoft.bookstore.domain.auth.provider.PreAuthenticatedAuthenticationProvider;
import com.github.fenixsoft.bookstore.domain.auth.provider.UsernamePasswordAuthenticationProvider;
import com.github.fenixsoft.bookstore.domain.auth.service.AuthenticAccountDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Spring Security的用户认证服务器配置
 * <p>
 * 借用Spring Security作为认证服务器，告知服务器通过怎样的途径去查询用户、加密密码和验证用户真伪
 * 我们实际上并不使用Spring Security提供的认证表单，而是选择了前端通过OAuth2的密码模式，在授权过程中同时完成认证
 * 由于服务端整套安全机制（方法授权判断、OAuth2密码模式的用户认证、密码的加密算法）仍然是构建在Spring Security基础之上
 * 所以我们的认证服务、用户信息服务仍然继承着Spring Security提供的基类，并在这里注册到Spring Security当中
 *
 * @author icyfenix@gmail.com
 * @date 2020/3/7 19:41
 **/
@Configuration
@EnableWebSecurity
public class AuthenticationServerConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private AuthenticAccountDetailsService authenticAccountDetailsService;

    @Autowired
    private UsernamePasswordAuthenticationProvider userProvider;

    @Autowired
    private PreAuthenticatedAuthenticationProvider preProvider;

    @Autowired
    private PasswordEncoder encoder;


    /**
     * 需要把AuthenticationManager主动暴漏出来
     * 以便在授权服务器{@link AuthorizationServerConfiguration}中可以使用它来完成用户名、密码的认证
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * 配置Spring Security的安全认证服务
     * Spring Security的Web安全设置，将在资源服务器配置{@link ResourceServerConfiguration}中完成
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(authenticAccountDetailsService).passwordEncoder(encoder);
        auth.authenticationProvider(userProvider);
        auth.authenticationProvider(preProvider);
    }
}
