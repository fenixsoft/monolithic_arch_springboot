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

package com.github.fenixsoft.bookstore.domain.payment;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * 商品库存的领域服务
 *
 * @author icyfenix@gmail.com
 * @date 2020/3/12 20:23
 **/
@Named
public class StockpileService {

    @Inject
    private StockpileRepository repository;

    /**
     * 根据产品查询库存
     */
    public Stockpile getByProductId(Integer productId) {
        return repository.findById(productId).orElseThrow();
    }

    /**
     * 货物售出
     * 从冻结状态的货物中扣减
     */
    public void decrease(Integer productId, Integer amount) {
        Stockpile stock = repository.findById(productId).orElseThrow();
        stock.decrease(amount);
        repository.save(stock);
    }

    /**
     * 货物增加
     * 增加指定数量货物至正常货物状态
     */
    public void increase(Integer productId, Integer amount) {
        Stockpile stock = repository.findById(productId).orElseThrow();
        stock.increase(amount);
        repository.save(stock);
    }


    /**
     * 货物冻结
     * 从正常货物中移动指定数量至冻结状态
     */
    public void frozen(Integer productId, Integer amount) {
        Stockpile stock = repository.findById(productId).orElseThrow();
        stock.frozen(amount);
        repository.save(stock);
    }

    /**
     * 货物解冻
     * 从冻结货物中移动指定数量至正常状态
     */
    public void thawed(Integer productId, Integer amount) {
        frozen(productId, -1 * amount);
    }

    /**
     * 设置货物数量
     */
    public void set(Integer productId, Integer amount) {
        Stockpile stock = repository.findById(productId).orElseThrow();
        stock.setAmount(amount);
        repository.save(stock);
    }

}
