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

package com.github.fenixsoft.bookstore.domain.account.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 代表用户必须与当前登陆的用户一致
 * 相当于使用Spring Security的@PreAuthorize("#{user.name == authentication.name}")的验证
 *
 * @author icyfenix@gmail.com
 * @date 2020/3/16 11:06
 **/
@Documented
@Retention(RUNTIME)
@Target({FIELD, METHOD, PARAMETER, TYPE})
@Constraint(validatedBy = AccountValidation.AuthenticatedAccountValidator.class)
public @interface AuthenticatedAccount {
    String message() default "不是当前登陆用户";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
