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

package com.github.fenixsoft.bookstore.infrastructure.configuration;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.ClassUtils;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.Path;
import javax.ws.rs.ext.Provider;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Jersey服务器配置
 * <p>
 * 使用Jersey来提供对JAX-RS（JSR 370：Java API for Restful Web Services）的支持
 * 这里设置了所有服务的前缀路径“restful”和restful服务资源的包路径
 *
 * @author icyfenix@gmail.com
 * @date 2020/3/6 21:10
 **/
@Configuration
@ApplicationPath("/restful")
public class JerseyConfiguration extends ResourceConfig {
    public JerseyConfiguration() {
        scanPackages("com.github.fenixsoft.bookstore.resource");
        scanPackages("com.github.fenixsoft.bookstore.infrastructure.jaxrs");
    }

    /**
     * Jersey的packages()方法在Jar形式运行下有问题，这里修理一下
     */
    private void scanPackages(String scanPackage) {
        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
        scanner.addIncludeFilter(new AnnotationTypeFilter(Path.class));
        scanner.addIncludeFilter(new AnnotationTypeFilter(Provider.class));
        this.registerClasses(scanner.findCandidateComponents(scanPackage).stream()
                .map(beanDefinition -> ClassUtils.resolveClassName(Objects.requireNonNull(beanDefinition.getBeanClassName()), this.getClassLoader()))
                .collect(Collectors.toSet()));
    }


}
