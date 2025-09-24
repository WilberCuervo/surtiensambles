package com.jtccia.embargos.security.config;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;


import jakarta.persistence.EntityManagerFactory;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef = "clasicPrimaryManagerFactory", transactionManagerRef = "clasicPrimaryTransactionManager", basePackages = "com.jtccia.embargos.security.clasic.dao")
public class SecurityDataSourceClasicConfig {
	
	@Autowired
	private Environment env;
	
	@Bean (name= "clasicDataSource")
	public DataSource dataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(env.getProperty("segundaryDB.datasource.driver-class-name"));
		dataSource.setUrl(env.getProperty("segundaryDB.datasource.url"));
		dataSource.setUsername(env.getProperty("segundaryDB.datasource.username"));
		dataSource.setPassword(env.getProperty("segundaryDB.datasource.password"));
		return dataSource;
	}
	
	@Bean(name= "clasicPrimaryManagerFactory")
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(){
		LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
		
		emf.setDataSource(dataSource());
		emf.setPackagesToScan("com.jtccia.embargos.security.clasic.entity");
		emf.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
		
		Map<String, Object> properties = new HashMap<>();
		
		properties.put("hibernate.hbm2ddl.auto", env.getProperty("segundaryDB.jpa.hibernate.ddl-auto"));
		properties.put("hibernate.show-sql", env.getProperty("segundaryDB.jpa.show-sql"));
		properties.put("hibernate.dialect", env.getProperty("segundaryDB.jpa.database-platform"));
		
		emf.setJpaPropertyMap(properties);
		return emf;
	}
	
	@Bean(name= "clasicPrimaryTransactionManager")
	public PlatformTransactionManager transactionManager() {
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
		return transactionManager;
	}

}
