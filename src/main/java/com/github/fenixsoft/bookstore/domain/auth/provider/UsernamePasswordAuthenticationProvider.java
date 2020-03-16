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

package com.github.fenixsoft.bookstore.domain.auth.provider;

import com.github.fenixsoft.bookstore.domain.auth.service.AuthenticAccountDetailsService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * 基于用户名、密码的身份认证器
 * 该身份认证器会被{@link AuthenticationManager}验证管理器调用
 * 验证管理器支持多种验证方式，这里基于用户名、密码的的身份认证是方式之一
 *
 * @author icyfenix@gmail.com
 * @date 2020/3/7 21:45
 */
@Named
public class UsernamePasswordAuthenticationProvider implements AuthenticationProvider {

    @Inject
    private AuthenticAccountDetailsService authenticAccountDetailsService;

    @Inject
    private PasswordEncoder passwordEncoder;

    /**
     * 认证处理
     * <p>
     * 根据用户名查询用户资料，对比资料中加密后的密码
     * 结果将返回一个Authentication的实现类（此处为UsernamePasswordAuthenticationToken）则代表认证成功
     * 返回null或者抛出AuthenticationException的子类异常则代表认证失败
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName().toLowerCase();
        String password = (String) authentication.getCredentials();
        // AuthenticationException的子类定义了多种认证失败的类型，这里仅处“理用户不存在”、“密码不正确”两种
        // 用户不存在的话会直接由loadUserByUsername()抛出异常
        UserDetails user = authenticAccountDetailsService.loadUserByUsername(username);
        if (!passwordEncoder.matches(password, user.getPassword())) throw new BadCredentialsException("密码不正确");
        // 认证通过，返回令牌
        return new UsernamePasswordAuthenticationToken(user, password, user.getAuthorities());

    }

    /**
     * 判断该验证器能处理哪些认证
     */
    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.equals(UsernamePasswordAuthenticationToken.class);
    }

}
