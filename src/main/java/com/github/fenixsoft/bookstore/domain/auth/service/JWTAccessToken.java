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

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT访问令牌
 * <p>
 * JWT令牌的结构为三部分组成：[令牌头（Header）].[负载信息（Payload）].[签名（Signature）]
 * 令牌头：定义了令牌的元数据，如令牌采用的签名算法，默认为HMAC SHA256算法
 * 负载信息：由签发者自定义的数据，一般会包括过期时间（Expire）、授权范围（Authority）、令牌ID编号（JTI）等
 * 签名：签名是使用私钥和头部指定的算法，前两部分进行的数字签名，防止数据被篡改。
 * 以上，令牌头和负载均为JSON结构，进行Base64URLEncode之后进行签名，然后用“.”连接，构成令牌报文
 * <p>
 * Spring Security OAuth2的{@link JwtAccessTokenConverter}提供了令牌的基础结构（令牌头、部分负载，如过期时间、JTI）的转换实现
 * 继承此类，在加入自己定义的负载信息即可使用。一般来说负载中至少要告知服务端当前用户是谁，但又不应存放过多信息导致HTTP Header过大，尤其不应存放敏感信息。
 *
 * @author icyfenix@gmail.com
 * @date 2020/3/9 9:46
 */
@Named
public class JWTAccessToken extends JwtAccessTokenConverter {

    // 签名私钥
    // 此处内容是我随便写的UUID，按照JWT约定默认是256Bit的，其实任何格式都可以，只是要注意保密，不要公开出去
    private static final String JWT_TOKEN_SIGNING_PRIVATE_KEY = "601304E0-8AD4-40B0-BD51-0B432DC47461";

    @Inject
    JWTAccessToken(UserDetailsService userDetailsService) {
        // 设置签名私钥
        setSigningKey(JWT_TOKEN_SIGNING_PRIVATE_KEY);
        // 设置从资源请求中带上来的JWT令牌转换回安全上下文中的用户信息的查询服务
        // 如果不设置该服务，则从JWT令牌获得的Principal就只有一个用户名（令牌中确实就只存了用户名）
        // 将用户用户信息查询服务提供给默认的令牌转换器，使得转换令牌时自动根据用户名还原出完整的用户对象
        // 这方便了后面编码（可以在直接获得登陆用户信息），但也稳定地为每次请求增加了一次（从数据库/缓存）查询，自行取舍
        DefaultUserAuthenticationConverter converter = new DefaultUserAuthenticationConverter();
        converter.setUserDetailsService(userDetailsService);
        ((DefaultAccessTokenConverter) getAccessTokenConverter()).setUserTokenConverter(converter);
    }

    /**
     * 增强令牌
     * 增强主要就是在令牌的负载中加入额外的信息
     */
    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        Authentication user = authentication.getUserAuthentication();
        String[] authorities = user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toArray(String[]::new);
        Map<String, Object> payLoad = new HashMap<>();
        // Spring Security OAuth的JWT令牌默认实现中就加入了一个“user_name”的项存储了当前用户名
        // 这里主要是出于演示Payload的用途，以及方便客户端获取（否则客户端要从令牌中解码Base64来获取），设置了一个“username”，两者的内容是一致的
        payLoad.put("username", user.getName());
        payLoad.put("authorities", authorities);
        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(payLoad);
        return super.enhance(accessToken, authentication);
    }
}
