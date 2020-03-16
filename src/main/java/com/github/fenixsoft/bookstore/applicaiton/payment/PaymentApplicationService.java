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

package com.github.fenixsoft.bookstore.applicaiton.payment;

import com.github.fenixsoft.bookstore.applicaiton.payment.dto.Settlement;
import com.github.fenixsoft.bookstore.domain.payment.Payment;
import com.github.fenixsoft.bookstore.domain.payment.PaymentService;
import com.github.fenixsoft.bookstore.domain.payment.Stockpile;
import com.github.fenixsoft.bookstore.domain.payment.StockpileService;
import com.github.fenixsoft.bookstore.domain.warehouse.ProductService;

import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;

/**
 * 支付应用务
 *
 * @author icyfenix@gmail.com
 * @date 2020/3/12 16:29
 **/
@Named
@Transactional
public class PaymentApplicationService {

    @Inject
    private PaymentService paymentService;

    @Inject
    private ProductService productService;

    @Inject
    private StockpileService stockpileService;

    /**
     * 根据结算清单的内容执行，生成对应的支付单
     */
    public Payment executeBySettlement(Settlement bill) {
        // 从服务中获取商品的价格，计算要支付的总价（安全原因，这个不能由客户端传上来）
        productService.replenishProductInformation(bill);
        // 冻结部分库存（保证有货提供）,生成付款单
        Payment payment = paymentService.producePayment(bill);
        // 设立解冻定时器（超时未支付则释放冻结的库存和资金）
        paymentService.setupAutoThawedTrigger(payment);
        return payment;
    }

    /**
     * 完成支付
     * 立即取消解冻定时器，执行扣减库存和资金
     */
    public void accomplishPayment(String payId) {
        paymentService.accomplish(payId);
    }

    /**
     * 取消支付
     * 立即触发解冻定时器，释放库存和资金
     */
    public void cancelPayment(String payId) {
        paymentService.cancel(payId);
    }

    /**
     * 根据产品查询库存
     */
    public Stockpile getStockpile(Integer productId) {
        return stockpileService.getByProductId(productId);
    }

    /**
     * 将指定的产品库存调整为指定数额
     */
    public void setStockpileAmountByProductId(Integer productId, Integer amount) {
        stockpileService.set(productId, amount);
    }

}
