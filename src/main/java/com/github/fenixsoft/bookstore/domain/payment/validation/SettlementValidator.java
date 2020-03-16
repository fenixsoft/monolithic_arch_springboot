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

package com.github.fenixsoft.bookstore.domain.payment.validation;

import com.github.fenixsoft.bookstore.applicaiton.payment.dto.Settlement;
import com.github.fenixsoft.bookstore.domain.payment.StockpileService;

import javax.inject.Inject;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 结算单验证器
 * <p>
 * 结算单能够成功执行的约束是清单中每一项商品的库存量都足够。
 * <p>
 * 这个验证器的目的不在于保证商品高并发情况（如秒杀活动）下不超卖，而在于避免库存不足时仍可下单。高并发下的超卖是一种“不可重复读”现象
 * （即读取过的数据在事务期间被另一个事务改变），如要严谨地避免，需要把数据库的隔离级别从默认的“Read Committed”提升至“Repeatable Read”
 * 除了MySQL（InnoDB）外，主流的数据库，如Oracle、SQLServer默认都是Read committed，提升隔离级别会显著影响数据库的并发能力。
 *
 * @author icyfenix@gmail.com
 * @date 2020/3/16 9:02
 **/
public class SettlementValidator implements ConstraintValidator<SufficientStock, Settlement> {

    @Inject
    private StockpileService service;

    @Override
    public boolean isValid(Settlement value, ConstraintValidatorContext context) {
        return value.getItems().stream().noneMatch(i -> service.getByProductId(i.getProductId()).getAmount() < i.getAmount());
    }
}
