package com.github.fenixsoft.bookstore.infrastructure.jaxrs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * 用于统一处理在Resource中由于Spring Security授权访问产生的异常信息
 *
 * @author icyfenix@gmail.com
 * @date 2020/4/7 0:09
 **/
@Provider
public class AccessDeniedExceptionMapper implements ExceptionMapper<AccessDeniedException> {

    private static final Logger log = LoggerFactory.getLogger(AccessDeniedExceptionMapper.class);

    @Context
    private HttpServletRequest request;

    @Override
    public Response toResponse(AccessDeniedException exception) {
        log.warn("越权访问被禁止 {}: {}", request.getMethod(), request.getPathInfo());
        return CommonResponse.send(Response.Status.FORBIDDEN, exception.getMessage());
    }
}
