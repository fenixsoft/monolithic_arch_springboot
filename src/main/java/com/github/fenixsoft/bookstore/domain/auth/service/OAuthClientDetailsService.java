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

package com.github.fenixsoft.bookstore.domain.auth.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.builders.InMemoryClientDetailsServiceBuilder;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * OAuth2客户端类型定义
 * <p>
 * OAuth2支持四种授权模式，这里仅定义了密码模式（Resource Owner Password Credentials Grant）一种
 * OAuth2作为开放的（面向不同服务提供商）授权协议，要求用户提供明文用户名、密码的这种“密码模式”并不常用
 * 而这里可以采用是因为前端（BookStore FrontEnd）与后端服务是属于同一个服务提供者的，实质上不存在密码会不会被第三方保存的敏感问题
 * 如果永远只考虑单体架构、单一服务提供者，则并无引入OAuth的必要，Spring Security的表单认证就能很良好、便捷地解决认证和授权的问题
 * 这里使用密码模式来解决，是为了下一阶段演示微服务化后，服务之间鉴权作准备，以便后续扩展以及对比。
 *
 * @author icyfenix@gmail.com
 * @date 2020/3/7 19:45
 **/
@Named
public class OAuthClientDetailsService implements ClientDetailsService {

    /**
     * 客户端ID
     * 这里的客户端就是指本项目的前端代码
     */
    private static final String CLIENT_ID = "bookstore_frontend";
    /**
     * 客户端密钥
     * 在OAuth2协议中，ID是可以公开的，密钥应当保密，密钥用以证明当前申请授权的客户端是未被冒充的
     */
    private static final String CLIENT_SECRET = "bookstore_secret";

    @Inject
    private PasswordEncoder passwordEncoder;

    private ClientDetailsService clientDetailsService;

    /**
     * 构造密码授权模式
     * <p>
     * 由于实质上只有一个客户端，所以就不考虑存储和客户端的增删改查了，直接在内存中配置出客户端的信息
     * <p>
     * 授权Endpoint示例：
     * /oauth/token?grant_type=password & username=#USER# & password=#PWD# & client_id=bookstore_frontend & client_secret=bookstore_secret
     * 刷新令牌Endpoint示例：
     * /oauth/token?grant_type=refresh_token & refresh_token=#REFRESH_TOKEN# & client_id=bookstore_frontend & client_secret=bookstore_secret
     */
    @PostConstruct
    public void init() throws Exception {
        InMemoryClientDetailsServiceBuilder builder = new InMemoryClientDetailsServiceBuilder();
        // 提供客户端ID和密钥，并指定该客户端支持密码授权、刷新令牌两种访问类型
        builder.withClient(CLIENT_ID)
                .secret(passwordEncoder.encode(CLIENT_SECRET))
                .scopes("BROWSER")
                .authorizedGrantTypes("password", "refresh_token");
        clientDetailsService = builder.build();
    }

    /**
     * 外部根据客户端ID查询验证方式
     */
    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        return clientDetailsService.loadClientByClientId(clientId);
    }
}
