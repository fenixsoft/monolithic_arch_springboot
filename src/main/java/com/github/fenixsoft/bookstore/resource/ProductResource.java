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

package com.github.fenixsoft.bookstore.resource;

import com.github.fenixsoft.bookstore.applicaiton.ProductApplicationService;
import com.github.fenixsoft.bookstore.domain.auth.Role;
import com.github.fenixsoft.bookstore.domain.warehouse.Product;
import com.github.fenixsoft.bookstore.infrastructure.jaxrs.CommonResponse;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Component;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * 产品相关的资源
 *
 * @author icyfenix@gmail.com
 * @date 2020/3/6 20:52
 **/

@Path("/products")
@Component
@CacheConfig(cacheNames = "resource.product")
@Produces(MediaType.APPLICATION_JSON)
public class ProductResource {

    @Inject
    ProductApplicationService service;

    /**
     * 获取仓库中所有的货物信息
     */
    @GET
    @Cacheable(key = "'ALL_PRODUCT'")
    public Iterable<Product> getAllProducts() {
        return service.getAllProducts();
    }

    /**
     * 获取仓库中指定的货物信息
     */
    @GET
    @Path("/{id}")
    @Cacheable(key = "#id")
    public Product getProduct(@PathParam("id") Integer id) {
        return service.getProduct(id);
    }

    /**
     * 更新产品信息
     */
    @PUT
    @Caching(evict = {
            @CacheEvict(key = "#product.id"),
            @CacheEvict(key = "'ALL_PRODUCT'")
    })
    @RolesAllowed(Role.ADMIN)
    public Response updateProduct(@Valid Product product) {
        return CommonResponse.op(() -> service.saveProduct(product));
    }

    /**
     * 创建新的产品
     */
    @POST
    @CacheEvict(key = "'ALL_PRODUCT'")
    @RolesAllowed(Role.ADMIN)
    public Product createProduct(@Valid Product product) {
        return service.saveProduct(product);
    }

    /**
     * 创建新的产品
     */
    @DELETE
    @Path("/{id}")
    @Caching(evict = {
            @CacheEvict(key = "#id"),
            @CacheEvict(key = "'ALL_PRODUCT'")
    })
    @RolesAllowed(Role.ADMIN)
    public Response removeProduct(@PathParam("id") Integer id) {
        return CommonResponse.op(() -> service.removeProduct(id));
    }


}
