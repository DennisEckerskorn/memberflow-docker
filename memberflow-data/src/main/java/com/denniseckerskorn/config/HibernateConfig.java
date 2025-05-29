package com.denniseckerskorn.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * Hibernate configuration class for setting up the EntityManagerFactory and DataSource.
 * It uses HikariCP for connection pooling and configures JPA properties.
 */
@Configuration
@EnableTransactionManagement
public class HibernateConfig {

    private final Environment env;

    /**
     * Constructor to inject the Environment object for accessing properties.
     *
     * @param env the Environment object containing application properties
     */
    public HibernateConfig(Environment env) {
        this.env = env;
    }

    /**
     * Bean definition for DataSource using HikariCP.
     * Reads properties from the application environment.
     *
     * @return a configured DataSource instance
     */
    @Bean
    public DataSource dataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(env.getProperty("spring.datasource.url"));
        dataSource.setUsername(env.getProperty("spring.datasource.username"));
        dataSource.setPassword(env.getProperty("spring.datasource.password"));
        dataSource.setDriverClassName(env.getProperty("spring.datasource.driver-class-name"));
        return dataSource;
    }

    /**
     * Bean definition for LocalContainerEntityManagerFactoryBean.
     *
     * @param dataSource the DataSource to be used by the EntityManagerFactory
     * @return a configured LocalContainerEntityManagerFactoryBean instance
     */
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
        emf.setDataSource(dataSource);
        emf.setPackagesToScan("com.denniseckerskorn.entities");
        emf.setJpaVendorAdapter(new HibernateJpaVendorAdapter());

        Properties jpaProperties = new Properties();
        jpaProperties.put("hibernate.dialect", env.getProperty("spring.jpa.database-platform"));
        jpaProperties.put("hibernate.hbm2ddl.auto", env.getProperty("spring.jpa.hibernate.ddl-auto"));
        jpaProperties.put("hibernate.show_sql", env.getProperty("spring.jpa.show-sql"));
        jpaProperties.put("hibernate.current_session_context_class", env.getProperty("spring.jpa.properties.hibernate.current_session_context_class"));
        emf.setJpaProperties(jpaProperties);

        return emf;
    }

    /**
     * Bean definition for PlatformTransactionManager.
     * Uses JpaTransactionManager to manage transactions.
     *
     * @param emf the LocalContainerEntityManagerFactoryBean to be used by the transaction manager
     * @return a configured PlatformTransactionManager instance
     */
    @Bean
    public PlatformTransactionManager transactionManager(LocalContainerEntityManagerFactoryBean emf) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(emf.getObject());
        return transactionManager;
    }
}