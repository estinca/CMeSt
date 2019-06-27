package com.est.repository.dbm.config;

import lombok.Data;

@Data
public class DataSourceConfig {
	
	private DatabaseType databaseType;
	private String host;
	private int port;
	private String databaseName;
	private boolean useSSL;
	private String serverTimezone;
	
	private String username;
	private String password;
	
	private String[] packagesToScan;
}
