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

package com.github.fenixsoft.bookstore.infrastructure.jaxrs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.function.Consumer;

/**
 * 为了简化编码而设计的HTTP Response对象包装类和工具集
 * <p>
 * 带有服务状态编码的（带有Code字段的）JavaBean领域对象包装类
 * Code字段的通常用于服务消费者判定该请求的业务处理是否成功。
 * <p>
 * 统一约定：
 * - 当服务调用正常完成，返回Code一律以0表示
 * - 当服务调用产生异常，可自定义不为0的Code值，此时Message字段作为返回客户端的详细信息
 *
 * @author icyfenix@gmail.com
 * @date 2020/3/6 15:46
 **/
public abstract class CommonResponse {

    private static final Logger log = LoggerFactory.getLogger(CommonResponse.class);

    /**
     * 向客户端发送自定义操作信息
     */
    public static Response send(Response.Status status, String message) {
        Integer code = status.getFamily() == Response.Status.Family.SUCCESSFUL ? CodedMessage.CODE_SUCCESS : CodedMessage.CODE_DEFAULT_FAILURE;
        return Response.status(status).type(MediaType.APPLICATION_JSON).entity(new CodedMessage(code, message)).build();
    }

    /**
     * 向客户端发送操作失败的信息
     */
    public static Response failure(String message) {
        return send(Response.Status.INTERNAL_SERVER_ERROR, message);
    }

    /**
     * 向客户端发送操作成功的信息
     */
    public static Response success(String message) {
        return send(Response.Status.OK, message);
    }

    /**
     * 向客户端发送操作成功的信息
     */
    public static Response success() {
        return send(Response.Status.OK, "操作已成功");
    }

    /**
     * 执行操作，并根据操作是否成功返回给客户端相应信息
     * 封装了在服务端接口中很常见的执行操作，成功返回成功标志、失败返回失败标志的通用操作，用于简化编码
     */
    public static Response op(Runnable executor) {
        return op(executor, e -> log.error(e.getMessage(), e));
    }

    /**
     * 执行操作（带自定义的失败处理），并根据操作是否成功返回给客户端相应信息
     * 封装了在服务端接口中很常见的执行操作，成功返回成功标志、失败返回失败标志的通用操作，用于简化编码
     */
    public static Response op(Runnable executor, Consumer<Exception> exceptionConsumer) {
        try {
            executor.run();
            return CommonResponse.success();
        } catch (Exception e) {
            exceptionConsumer.accept(e);
            return CommonResponse.failure(e.getMessage());
        }
    }

}
