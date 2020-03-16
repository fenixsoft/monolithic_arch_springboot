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

import com.github.fenixsoft.bookstore.domain.warehouse.Advertisement;
import com.github.fenixsoft.bookstore.domain.warehouse.AdvertisementRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * 广告相关的资源
 *
 * @author icyfenix@gmail.com
 * @date 2020/3/7 10:48
 **/
@Path("/advertisements")
@Component
@Produces(MediaType.APPLICATION_JSON)
public class AdvertisementResource {

    @Inject
    AdvertisementRepository repository;

    @GET
    @Cacheable("resource.advertisements")
    public Iterable<Advertisement> getAllAdvertisements() {
        return repository.findAll();
    }
}
