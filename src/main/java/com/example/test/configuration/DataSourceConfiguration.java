package com.example.test.configuration;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.orm.jpa.hibernate.SpringImplicitNamingStrategy;
import org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.example.test.constants.Constants;

import liquibase.integration.spring.SpringLiquibase;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef = "entityManagerFactory", transactionManagerRef = "transactionManager", basePackages = "com.example.test.repository")
public class DataSourceConfiguration {
	
	@Value("${user.datasource.username}")
	private String username;

	@Value("${user.datasource.username}")
	private String username2;
	

	@Value("${user.datasource.password}")
	private String password;

	@Value("${user.datasource.password}")
	private String password2;
	
	@Value("${user.datasource.url}")
	private String dbUrl;

	@Value("${user2.datasource.url}")
	private String dbUrl2;

	@Primary
	@Bean
	@Qualifier(value = "transactionManager")
	PlatformTransactionManager transactionManager(
			@Qualifier(value = "entityManagerFactory") final EntityManagerFactory entityManagerFactory) {
		return new JpaTransactionManager(entityManagerFactory);
	}

	@Primary
	@Bean
	@Qualifier(value = "entityManagerFactory")
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(
			@Qualifier(value = "dataSource") final DataSource dataSource) {
		HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
		Properties properties = new Properties();
		properties.put("hibernate.hbm2ddl.auto", "update");
		properties.put("hibernate.dialect", "org.hibernate.dialect.MySQL5InnoDBDialect");
		properties.put("hibernate.physical_naming_strategy", SpringPhysicalNamingStrategy.class.getName());
		properties.put("hibernate.implicit_naming_strategy", SpringImplicitNamingStrategy.class.getName());
		LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
		factoryBean.setDataSource(dataSource);
		factoryBean.setJpaVendorAdapter(jpaVendorAdapter);
		factoryBean.setJpaProperties(properties);
		factoryBean.setPackagesToScan("com.example.test.entity");
		return factoryBean;
	}

	@Bean
	@Qualifier(value = "user2EntityManagerFactory")
	public LocalContainerEntityManagerFactoryBean user2EntityManagerFactory(
			@Qualifier(value = "entityManagerFactory") final LocalContainerEntityManagerFactoryBean entityManagerFactory) {

		ContextHolder.set(Database.USER2);
		return entityManagerFactory;
	}

	@Primary
	@Bean
	@Qualifier(value = "dataSource")
	public CustomRoutingDataSource dataSource() {

		CustomRoutingDataSource customRoutingDataSource = new CustomRoutingDataSource();

		DataSource defaultDataSource = userDataSource();
		customRoutingDataSource.setDefaultTargetDataSource(defaultDataSource);
		Map<Object, Object> dataSources = new LinkedHashMap<>();
		dataSources.put(Database.USER, defaultDataSource);
		dataSources.put(Database.USER2, user2DataSource());
		customRoutingDataSource.setTargetDataSources(dataSources);

		return customRoutingDataSource;
	}

	public DataSource userDataSource() {

		return datasourceConfig(dbUrl, username, password);
	}

	public DataSource user2DataSource() {
		return datasourceConfig(dbUrl2, username2, password2);
	}


	private DriverManagerDataSource datasourceConfig(final String dbUrl, final String username, final String password) {

		DriverManagerDataSource dataSource = new DriverManagerDataSource(dbUrl, username, password);
		dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
		return dataSource;
	}

	@Primary
	@Bean(name = "userLiquibase")
	@DependsOn(value = "entityManagerFactory")
	public SpringLiquibase userLiquibase(@Qualifier(value = "dataSource") final DataSource dataSource) {
		Map<String, String> parameters = new LinkedHashMap<>();
		parameters.put("db", "user");
		parameters.put("internal_username", username);
		SpringLiquibase liquibase = liquibaseProperties(dataSource, parameters);
		ContextHolder.set(Database.USER);
		return liquibase;
	}


	@Bean(name = "user2Liquibase")
	@DependsOn(value = "user2EntityManagerFactory")
	public SpringLiquibase clientLiquibase(@Qualifier(value = "dataSource") final DataSource dataSource) {
		Map<String, String> parameters = new LinkedHashMap<>();
		parameters.put("db", "user2");
		parameters.put("internal_username", username);
		SpringLiquibase liquibase = liquibaseProperties(dataSource, parameters);
		ContextHolder.set(Database.USER2);
		return liquibase;
	}



	private SpringLiquibase liquibaseProperties(final DataSource dataSource, final Map<String, String> parameters) {

		LiquibaseProperties liquibaseProperties = new LiquibaseProperties();
		SpringLiquibase liquibase = new SpringLiquibase();
		liquibase.setChangeLog(Constants.CHANGE_LOG);

		liquibase.setContexts(liquibaseProperties.getContexts());
		liquibase.setDataSource(dataSource);
		liquibase.setChangeLogParameters(parameters);
		return liquibase;
	}

}