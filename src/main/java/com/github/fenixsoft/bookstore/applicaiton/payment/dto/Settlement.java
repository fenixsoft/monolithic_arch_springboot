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

package com.github.fenixsoft.bookstore.applicaiton.payment.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.fenixsoft.bookstore.domain.warehouse.Product;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.Map;

/**
 * 支付结算单模型
 *
 * @author icyfenix@gmail.com
 * @date 2020/3/12 11:35
 **/
public class Settlement {

    @Size(min = 1, message = "结算单中缺少商品清单")
    private Collection<Item> items;

    @NotNull(message = "结算单中缺少配送信息")
    private Purchase purchase;

    /**
     * 购物清单中的商品信息
     * 基于安全原因（避免篡改价格），改信息不会取客户端的，需在服务端根据商品ID再查询出来
     */
    public transient Map<Integer, Product> productMap;

    public Collection<Item> getItems() {
        return items;
    }

    public void setItems(Collection<Item> items) {
        this.items = items;
    }

    public Purchase getPurchase() {
        return purchase;
    }

    public void setPurchase(Purchase purchase) {
        this.purchase = purchase;
    }

    /**
     * 结算单中要购买的商品
     */
    public static class Item {
        @NotNull(message = "结算单中必须有明确的商品数量")
        @Min(value = 1, message = "结算单中商品数量至少为一件")
        private Integer amount;

        @JsonProperty("id")
        @NotNull(message = "结算单中必须有明确的商品信息")
        private Integer productId;

        public Integer getAmount() {
            return amount;
        }

        public void setAmount(Integer amount) {
            this.amount = amount;
        }

        public Integer getProductId() {
            return productId;
        }

        public void setProductId(Integer productId) {
            this.productId = productId;
        }

    }

    /**
     * 结算单中的配送信息
     */
    public static class Purchase {

        private Boolean delivery = true;

        @NotEmpty(message = "配送信息中缺少支付方式")
        private String pay;

        @NotEmpty(message = "配送信息中缺少收件人姓名")
        private String name;

        @NotEmpty(message = "配送信息中缺少收件人电话")
        private String telephone;

        @NotEmpty(message = "配送信息中缺少收件地址")
        private String location;

        public Boolean getDelivery() {
            return delivery;
        }

        public void setDelivery(Boolean delivery) {
            this.delivery = delivery;
        }

        public String getPay() {
            return pay;
        }

        public void setPay(String pay) {
            this.pay = pay;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getTelephone() {
            return telephone;
        }

        public void setTelephone(String telephone) {
            this.telephone = telephone;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }
    }

}
