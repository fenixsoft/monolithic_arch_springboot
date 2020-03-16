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

package com.github.fenixsoft.bookstore.domain.auth;

import com.github.fenixsoft.bookstore.domain.account.Account;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;

/**
 * 认证用户模型
 * <p>
 * 用户注册之后，包含其业务属性，如姓名、电话、地址，用于业务发生，存储于Account对象中
 * 也包含其用于认证的属性，譬如密码、角色、是否停用，存储于AuthenticAccount对象中
 *
 * @author icyfenix@gmail.com
 * @date 2020/3/7 20:46
 **/
public class AuthenticAccount extends Account implements UserDetails {

    public AuthenticAccount() {
        super();
        authorities.add(new SimpleGrantedAuthority(Role.USER));
    }

    public AuthenticAccount(Account origin) {
        this();
        BeanUtils.copyProperties(origin, this);
        if (getId() == 1) {
            // 由于没有做用户管理功能，默认给系统中第一个用户赋予管理员角色
            authorities.add(new SimpleGrantedAuthority(Role.ADMIN));
        }
    }

    /**
     * 该用户拥有的授权，譬如读取权限、修改权限、增加权限等等
     */
    private Collection<GrantedAuthority> authorities = new HashSet<>();

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Collection<GrantedAuthority> authorities) {
        this.authorities = authorities;
    }

    /**
     * 账号是否过期
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 是否锁定
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * 密码是否过期
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 是否被锁定
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
