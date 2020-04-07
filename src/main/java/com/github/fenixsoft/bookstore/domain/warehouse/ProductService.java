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

package com.github.fenixsoft.bookstore.domain.warehouse;

import com.github.fenixsoft.bookstore.applicaiton.payment.dto.Settlement;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 产品领域服务
 *
 * @author icyfenix@gmail.com
 * @date 2020/3/12 20:58
 **/
@Named
public class ProductService {

    @Inject
    private ProductRepository repository;

    /**
     * 根据结算单中货物的ID，填充货物的完整信息到结算单对象上
     */
    public void replenishProductInformation(Settlement bill) {
        List<Integer> ids = bill.getItems().stream().map(Settlement.Item::getProductId).collect(Collectors.toList());
        bill.productMap = repository.findByIdIn(ids).stream().collect(Collectors.toMap(Product::getId, Function.identity()));
    }

    /**
     * 获取仓库中所有的货物信息
     */
    public Iterable<Product> getAllProducts() {
        return repository.findAll();
    }

    /**
     * 获取仓库中指定的货物信息
     */
    public Product getProduct(Integer id) {
        return repository.findById(id).orElse(null);
    }

    /**
     * 创建或者更新产品信息
     */
    public Product saveProduct(Product product) {
        return repository.save(product);
    }

    /**
     * 删除指定产品
     */
    public void removeProduct(Integer id) {
        repository.deleteById(id);
    }

}
