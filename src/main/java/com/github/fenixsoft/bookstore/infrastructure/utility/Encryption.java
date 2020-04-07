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

package com.github.fenixsoft.bookstore.infrastructure.utility;

import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.inject.Named;
import java.util.Optional;

/**
 * 默认的加密工具
 *
 * @author icyfenix@gmail.com
 * @date 2020/3/10 18:02
 **/
@Named
public class Encryption {

    /**
     * 配置认证使用的密码加密算法：BCrypt
     * 由于在Spring Security很多验证器中都要用到{@link PasswordEncoder}的加密，所以这里要添加@Bean注解发布出去
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    /**
     * 使用默认加密算法进行编码
     */
    public String encode(CharSequence rawPassword) {
        return passwordEncoder().encode(Optional.ofNullable(rawPassword).orElse(""));
    }

}
