package com.saber.camel_spring_crud_server.config;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.apache.camel.component.sql.SqlComponent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import java.beans.PropertyVetoException;

@Configuration
public class DataBaseConfig {
	
	@Value(value = "${spring.datasource.driver-class-name}")
	private String driverClassName;
	@Value(value = "${spring.datasource.url}")
	private String jdbcUrl;
	@Value(value = "${spring.datasource.username}")
	private String username;
	@Value(value = "${spring.datasource.password}")
	private String password;
	@Value(value = "${spring.datasource.minPoolSize}")
	private int minPoolSize;
	@Value(value = "${spring.datasource.maxPoolSize}")
	private int maxPoolSize;
	@Value(value = "${spring.datasource.initialPoolSize}")
	private int initialPoolSize;
	@Value(value = "${spring.datasource.maxStatementsPerConnection}")
	private int maxStatementsPerConnection;
	@Value(value = "${spring.datasource.maxIdleTime}")
	private int maxIdleTime;
	@Value(value = "${spring.datasource.acquireIncrement}")
	private int acquireIncrement;
	@Value(value = "${spring.datasource.maxConnectionAge}")
	private int maxConnectionAge;
	@Value(value = "${spring.datasource.numHelperThreads}")
	private int numHelperThreads;
	@Value(value = "${spring.datasource.idleConnectionTestPeriod}")
	private int idleConnectionTestPeriod;
	@Value(value = "${spring.datasource.checkoutTimeout}")
	private int checkoutTimeout;
	@Value(value = "${spring.datasource.testConnectionOnCheckin}")
	private boolean testConnectionOnCheckin;
	@Value(value = "${spring.datasource.testConnectionOnCheckout}")
	private boolean testConnectionOnCheckout;
	@Value(value = "${spring.datasource.preferredTestQuery}")
	private String preferredTestQuery;
	
	@Bean("mysqlDataSource")
	@Primary
	public ComboPooledDataSource dpiDataSource() throws PropertyVetoException {
		
		ComboPooledDataSource dataSource = new ComboPooledDataSource();
		dataSource.setDriverClass(driverClassName);
		dataSource.setAutoCommitOnClose(false);
		dataSource.setJdbcUrl(jdbcUrl);
		dataSource.setUser(username);
		dataSource.setPassword(password);
		dataSource.setMinPoolSize(minPoolSize);
		dataSource.setMaxPoolSize(maxPoolSize);
		dataSource.setInitialPoolSize(initialPoolSize);
		dataSource.setMaxStatementsPerConnection(maxStatementsPerConnection);
		
		dataSource.setMaxIdleTime(maxIdleTime);
		dataSource.setAcquireIncrement(acquireIncrement);
		dataSource.setMaxConnectionAge(maxConnectionAge);
		dataSource.setNumHelperThreads(numHelperThreads);
		dataSource.setPreferredTestQuery(preferredTestQuery);
		dataSource.setIdleConnectionTestPeriod(idleConnectionTestPeriod);
		dataSource.setCheckoutTimeout(checkoutTimeout);
		dataSource.setTestConnectionOnCheckin(testConnectionOnCheckin);
		dataSource.setTestConnectionOnCheckout(testConnectionOnCheckout);
		
		return dataSource;
	}
	@Bean(value = "sql")
	public SqlComponent sql() throws PropertyVetoException {
		SqlComponent sql = new SqlComponent();
		sql.setDataSource(dpiDataSource());
		sql.setUsePlaceholder(true);
		return sql;
	}
}
