package com.example.testfetchdata.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.*;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.sql.DataSource;
import java.util.HashMap;

@Configuration
@EnableJpaRepositories(
        basePackages = {
                "com.example.testfetchdata.feature.*.repository"
        },
        entityManagerFactoryRef = "entityManager",
        transactionManagerRef = "transactionManager",
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE)
        }
)
public class DatabaseConfig {
    @Bean
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource.test.jpa")
    public JpaProperties getJpaProperty() {
        return new JpaProperties();
    }

    @Bean(name = "dataSourceProperties")
    @Primary
    @ConfigurationProperties("spring.datasource.test")
    public DataSourceProperties dataSourceProperties() {
        return new DataSourceProperties();
    }
    @Bean(name = "entityManager")
    @Primary
    public LocalContainerEntityManagerFactoryBean entityManager() {
        LocalContainerEntityManagerFactoryBean em
                = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(projectionDataSource());
        em.setPackagesToScan(
                "com.example.testfetchdata.feature.*.entity");

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        HashMap<String, Object> properties = new HashMap<>();
        JpaProperties jpaProperties = getJpaProperty();
        properties.put("hibernate.dialect", jpaProperties.getDatabasePlatform());
        properties.put("hibernate.show_sql", jpaProperties.isShowSql());
        properties.put("hibernate.generate-ddl", jpaProperties.isGenerateDdl());
        properties.put("hibernate.hbm2ddl.auto", jpaProperties.getProperties().getOrDefault("ddl-auto", "none"));
        properties.put("hibernate.jdbc.batch_size", jpaProperties.getProperties().getOrDefault("jdbc.batch_size", "1000"));
        properties.put("hibernate.jdbc.batch_versioned_data", jpaProperties.getProperties().getOrDefault("jdbc.batch_versioned_data", "true"));
        properties.put("hibernate.order_inserts", jpaProperties.getProperties().getOrDefault("order_inserts", "true"));
        properties.put("hibernate.order_updates", jpaProperties.getProperties().getOrDefault("order_updates", "true"));
        properties.put("hibernate.generate_statistics", jpaProperties.getProperties().getOrDefault("hibernate.generate_statistics", "false"));
        em.setJpaPropertyMap(properties);
        return em;
    }

    @Bean(name = "testDataSource")
    @Primary
    @ConfigurationProperties("spring.datasource.test.hikari")
    public DataSource projectionDataSource() {
        return dataSourceProperties().initializeDataSourceBuilder().type(HikariDataSource.class).build();
    }
    

}
