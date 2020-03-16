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

import javax.inject.Named;

/**
 * 用户钱包的领域服务
 *
 * @author icyfenix@gmail.com
 * @date 2020/3/12 20:23
 **/
@Named
public class WalletService {


    /**
     * 账户资金减少
     * 从冻结状态的资金中扣减
     */
    public void decrease(Integer accountId, String amount) {

    }

    /**
     * 账户资金增加
     * 增加指定数量资金至正常状态
     */
    public void increase(Integer accountId, String amount) {
    }

    /**
     * 账户资金冻结
     * 从正常资金中移动指定数量至冻结状态
     */
    public void frozen(Integer accountId, String amount) {
    }

    /**
     * 账户资金解冻
     * 从冻结资金中移动指定数量至正常状态
     */
    public void thawed(Integer accountId, String amount) {
    }


}
