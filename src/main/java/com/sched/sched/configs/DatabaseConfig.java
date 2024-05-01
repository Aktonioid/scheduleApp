package com.sched.sched.configs;

import java.util.Properties;

import javax.sql.DataSource;

import org.apache.tomcat.dbcp.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages = "com.sched.sched")
public class DatabaseConfig {
    
    @Autowired
    Environment env;

    @Bean
    public DataSource DataSource()
    {
        BasicDataSource dataSource = new BasicDataSource();
    
        dataSource.setDriverClassName(env.getProperty("jdbc.driverClassName"));
        dataSource.setPassword(env.getProperty("jdbc.password"));
        dataSource.setUsername(env.getProperty("jdbc.username"));
        dataSource.setUrl(env.getProperty("jdbc.url"));
    
        return dataSource;
    }

    Properties HibernateProperties()
    {
        Properties  properties = new Properties();

        properties.put("hibernate.dialect",env.getProperty("hibernate.dialect"));
        properties.put("hibernate.show_sql", env.getProperty("hibernate.show_sql"));
        properties.put("hibernate.format_sql", env.getProperty("hibernate.format_sql"));
        properties.put("hibernate.highlight_sql", env.getProperty("hibernate.highlight_sql"));
        properties.put("logging.level.org.hibernate.sql", env.getProperty("logging.level.org.hibernate.sql"));
        properties.put("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto"));

        return properties;
    }

    @Bean
    public LocalSessionFactoryBean SessionFactory()
    {
        LocalSessionFactoryBean sessionFactoryBean = new LocalSessionFactoryBean();
        sessionFactoryBean.setDataSource(DataSource());
        sessionFactoryBean.setPackagesToScan(new String[]
        {
            "com.sched.sched.core.models"
        }
        );
        sessionFactoryBean.setHibernateProperties(HibernateProperties());

        return sessionFactoryBean;
    }

    @Bean
    public HibernateTransactionManager transactionManager()
    {
        HibernateTransactionManager transactionManager = new HibernateTransactionManager();
        transactionManager.setSessionFactory(SessionFactory().getObject());
    
        return transactionManager;
    }
}
