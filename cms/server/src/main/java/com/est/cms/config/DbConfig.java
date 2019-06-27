package com.est.cms.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("DBM")
@Import(com.est.repository.dbm.config.DbConfig.class)
public class DbConfig {

}
