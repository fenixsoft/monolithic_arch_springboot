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

import com.github.fenixsoft.bookstore.domain.BaseEntity;
import com.github.fenixsoft.bookstore.domain.account.Account;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.persistence.Entity;
import java.util.Date;
import java.util.UUID;

/**
 * 支付单模型
 * <p>
 * 就是传到客户端让用户给扫码或者其他别的方式付钱的对象
 *
 * @author icyfenix@gmail.com
 * @date 2020/3/12 17:07
 **/
@Entity
public class Payment extends BaseEntity {

    /**
     * 支付状态
     */
    public enum State {
        /**
         * 等待支付中
         */
        WAITING,
        /**
         * 已取消
         */
        CANCEL,
        /**
         * 已支付
         */
        PAYED,
        /**
         * 已超时回滚（未支付，并且商品已恢复）
         */
        TIMEOUT
    }

    public Payment() {
    }

    public Payment(Double totalPrice, Long expires) {
        setTotalPrice(totalPrice);
        setExpires(expires);
        setCreateTime(new Date());
        setPayState(State.WAITING);
        // 下面这两个是随便写的，实际应该根据情况调用支付服务，返回待支付的ID
        setPayId(UUID.randomUUID().toString());
        // 产生支付单的时候一定是有用户的
        Account account = (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        setPaymentLink("/pay/modify/" + getPayId() + "?state=PAYED&accountId=" + account.getId());
    }

    private Date createTime;

    private String payId;

    private Double totalPrice;

    private Long expires;

    private String paymentLink;

    private State payState;

    public String getPayId() {
        return payId;
    }

    public void setPayId(String payId) {
        this.payId = payId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getExpires() {
        return expires;
    }

    public void setExpires(Long expires) {
        this.expires = expires;
    }

    public String getPaymentLink() {
        return paymentLink;
    }

    public void setPaymentLink(String paymentLink) {
        this.paymentLink = paymentLink;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public State getPayState() {
        return payState;
    }

    public void setPayState(State payState) {
        this.payState = payState;
    }
}
