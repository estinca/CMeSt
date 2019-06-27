package com.est.repository.dbm.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import com.est.utils.config.JsonPropertiesConfigUtils;
import com.est.utils.config.exception.JsonConfigException;

@Profile("DBM")
@Configuration
@ComponentScan(basePackages = { "com.est.repository.dbm" })
@EntityScan(basePackages = { "com.est.repository.dbm.domain" })
@EnableJpaRepositories(basePackages = { "com.est.repository.dbm.dao" })
public class DbConfig {

	private DataSourceConfig config;

	@PostConstruct
	public void init() throws JsonConfigException {
		config = JsonPropertiesConfigUtils.loadJsonConfig("config/dataSource.json", DataSourceConfig.class);
	}
	
    @Bean
    @Primary
    public DataSource dataSource() {
    	
        return DataSourceBuilder.create()
                .url(buildConnectionUrl(config))
                .username(config.getUsername())
                .password(config.getPassword())
                .driverClassName(getDatabaseDriver(config.getDatabaseType()))
                .build();
    }
    
	@Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
        emf.setDataSource(dataSource());
        emf.setPackagesToScan(getEmfPackagesToScan(config));
        emf.setPersistenceUnitName("est_cms");
        
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setShowSql(true);
        vendorAdapter.setGenerateDdl(false);

        Properties jpaProperties = new Properties();
        jpaProperties.put("hibernate.dialect", getHibernateDialect(config.getDatabaseType()));
        jpaProperties.put("hibernate.hbm2ddl.auto", "update");
        
        emf.setJpaVendorAdapter(vendorAdapter);
        emf.setJpaProperties(jpaProperties);
        emf.afterPropertiesSet();
        
        return emf;
	}
	
    private String[] getEmfPackagesToScan(DataSourceConfig config) {
        List<String> packagesToScan = new ArrayList<>();
        packagesToScan.add("com.est.repository.dbm.domain");
        packagesToScan.addAll(Arrays.asList(config.getPackagesToScan()));
    	
        return packagesToScan.toArray(new String[0]);
    }
    
    
	@Bean(name = "rawSettingsMap")
    public Map<String, String> rawSettingMap() {
        Map<String, String> settings = new HashMap<>();
        
        settings.put("connection.driver_class", getDatabaseDriver(config.getDatabaseType()));
        settings.put("dialect", getHibernateDialect(config.getDatabaseType()));
        settings.put("hibernate.connection.url", buildConnectionUrl(config));
        settings.put("hibernate.connection.username", config.getUsername());
        settings.put("hibernate.connection.password", config.getPassword());
        settings.put("show_sql", "true");

        return settings;
	}
	 
	
	/**
	 * Builds database url connection
	 * @param config Configuration properties 
	 * @return connection url string
	 */
	private String buildConnectionUrl(DataSourceConfig config) {
		StringBuilder stringBuilder = new StringBuilder("jdbc:");
		stringBuilder.append(getConnectionUrlType(config.getDatabaseType()))
		.append("://").append(config.getHost())
		.append(":").append(config.getPort())
		.append("/").append(config.getDatabaseName())
		.append("?useSSL=").append(config.isUseSSL())
		.append("&serverTimezone=").append(config.getServerTimezone());
		
		return stringBuilder.toString();
	}

	
	
    private String getHibernateDialect(DatabaseType databaseType) {
        switch(databaseType) {
            case POSTGRES:
                return "org.hibernate.dialect.PostgreSQL95Dialect";
            case MYSQL:
                default:
                    return "org.hibernate.dialect.MySQL8Dialect";
        }
    }
    

	private String getDatabaseDriver(DatabaseType databaseType) {
		switch (databaseType) {
		case POSTGRES:
			return "org.postgres.Driver";
		case MYSQL:
			default:
			return "com.mysql.cj.jdbc.Driver";
		}
	}
	
	
	private String getConnectionUrlType(DatabaseType databaseType) {
		switch (databaseType) {
		case POSTGRES:
			return "postgres";
		case MYSQL:
			default:
			return "mysql";
		}
	}
}
