package com.docelectronico.security.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariDataSource;
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
		entityManagerFactoryRef = "primaryManagerFactory", 
		transactionManagerRef = "primaryTransactionManager", 
		basePackages = "com.docelectronico.security.dao")
public class SecurityDataSourseConfig {
	
		@Autowired
		private Environment env;

		@Bean(name = "dataSource")
		public HikariDataSource dataSource() {
			HikariDataSource dataSource = new HikariDataSource();

			dataSource.setDriverClassName(env.getProperty("primaryDB.datasource.driver-class-name"));
			dataSource.setJdbcUrl(env.getProperty("primaryDB.datasource.url"));
			dataSource.setUsername(env.getProperty("primaryDB.datasource.username"));
			dataSource.setPassword(env.getProperty("primaryDB.datasource.password"));

//	        dataSource.setMaximumPoolSize(env.getProperty("primaryDB.datasource.hikari.max-pool-size"));
//	        dataSource.setConnectionTimeout(env.getProperty("primaryDB.datasource.hikari.connection-timeout"));

			return dataSource;
		}

		@Bean(name = "primaryManagerFactory")
		public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
			LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();

			emf.setDataSource(dataSource());
			emf.setPackagesToScan("com.docelectronico.security.entity");
			emf.setJpaVendorAdapter(new HibernateJpaVendorAdapter());

			Map<String, Object> properties = new HashMap<>();

			properties.put("hibernate.hbm2ddl.auto", env.getProperty("primaryDB.jpa.hibernate.ddl-auto"));
			properties.put("hibernate.show-sql", env.getProperty("primaryDB.jpa.show-sql"));
			properties.put("hibernate.dialect", env.getProperty("primaryDB.jpa.database-platform"));
			emf.setJpaPropertyMap(properties);
			return emf;
		}

		@Bean(name = "primaryTransactionManager")
		public PlatformTransactionManager transactionManager() {
			JpaTransactionManager transactionManager = new JpaTransactionManager();
			transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());

			return transactionManager;
		}
	


}
