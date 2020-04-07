package com.github.fenixsoft.bookstore;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * 单元测试基类
 * <p>
 * 提供了每个单元测试自动恢复数据库、清理缓存的处理
 *
 * @author icyfenix@gmail.com
 * @date 2020/4/7 14:19
 **/
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@Sql(scripts = {"classpath:db/hsqldb/schema.sql", "classpath:db/hsqldb/data.sql"})
@SpringBootTest(classes = BookstoreApplication.class)
public class DBRollbackBase {

    @Autowired
    private CacheManager cacheManager;

    @BeforeEach
    public void evictAllCaches() {
        for (String name : cacheManager.getCacheNames()) {
            cacheManager.getCache(name).clear();
        }
    }
}
