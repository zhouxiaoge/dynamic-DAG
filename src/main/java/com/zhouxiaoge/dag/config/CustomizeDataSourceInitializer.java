package com.zhouxiaoge.dag.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.sql.DataSource;

/**
 * springboot的启动时执行sql
 *
 * @author 周小哥
 * @date 2021年01月17日 20点15分
 */
@Configuration
public class CustomizeDataSourceInitializer {

    @Value("classpath:db/schema-mysql.sql")
    private Resource databaseSchema;

    @Value("classpath:db/data-mysql.sql")
    private Resource databaseData;

    @Bean
    public DataSourceInitializer dataSourceInitializer(final DataSource dataSource) {
        final DataSourceInitializer initializer = new DataSourceInitializer();
        initializer.setDataSource(dataSource);
        initializer.setDatabasePopulator(databasePopulator());
        return initializer;
    }

    private DatabasePopulator databasePopulator() {
        ResourceDatabasePopulator resourceDatabasePopulator = new ResourceDatabasePopulator();
        resourceDatabasePopulator.addScripts(databaseSchema);
        resourceDatabasePopulator.addScripts(databaseData);
        return resourceDatabasePopulator;
    }
}
