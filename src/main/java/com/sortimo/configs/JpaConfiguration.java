//package com.sortimo.configs;
//
//import java.util.Properties;
//
//import javax.sql.DataSource;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Primary;
//import org.springframework.jdbc.datasource.DriverManagerDataSource;
//import org.springframework.orm.jpa.JpaVendorAdapter;
//import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
//import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
//import org.springframework.stereotype.Component;
//
//@Component
//public class JpaConfiguration {
//
//    @Value("${db.driver}")
//    private String DB_DRIVER;
//
//    @Value("${db.password}")
//    private String DB_PASSWORD;
//
//    @Value("${db.url}")
//    private String DB_URL;
//
//    @Value("${db.username}")
//    private String DB_USERNAME;
//    
//    @Bean
//    @Primary
//    public DataSource dataSource() {
//        DriverManagerDataSource dataSource = new DriverManagerDataSource();
//        dataSource.setDriverClassName(DB_DRIVER);
//        dataSource.setUrl(DB_URL);
//        dataSource.setUsername(DB_USERNAME);
//        dataSource.setPassword(DB_PASSWORD);
//        return dataSource;
//    }
//
//    @Bean
//    public JpaVendorAdapter jpaVendorAdapter() {
//        HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
//        jpaVendorAdapter.setGenerateDdl(true);
//        jpaVendorAdapter.setShowSql(true);
//        jpaVendorAdapter.setDatabasePlatform("org.hibernate.dialect.MySQL5Dialect");
//
//        return jpaVendorAdapter;
//    }  
//
//    @Bean
//    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
//
//        LocalContainerEntityManagerFactoryBean lef = new LocalContainerEntityManagerFactoryBean();
//        lef.setPackagesToScan("com.sortimo.model");
//        lef.setDataSource(dataSource());
//        lef.setJpaVendorAdapter(jpaVendorAdapter());
//
//        Properties properties = new Properties();
//        properties.setProperty("hibernate.show_sql", "true");
//        properties.setProperty("hibernate.jdbc.fetch_size", "100");
//        properties.setProperty("hibernate.hbm2ddl.auto", "update");
//
//        lef.setJpaProperties(properties);
//        return lef;
//    }    
//
//
//}