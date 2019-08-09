package com.est.repository.dbm.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.Map;

import javax.sql.DataSource;

import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.hibernate.tool.schema.TargetType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.est.repository.api.model.Role;
import com.est.repository.api.model.Site;
import com.est.repository.api.model.User;
import com.est.repository.api.service.SetupService;
import com.est.repository.api.service.SiteService;
import com.est.repository.api.service.UserService;
import com.est.repository.dbm.domain.PageDB;
import com.est.repository.dbm.domain.SiteDB;
import com.est.repository.dbm.domain.UserDB;

import lombok.var;


@Service
public class SetupServiceDBM implements SetupService {

    private static final String[] TABLES_NEEDED = {
            "users",
            "user_roles",
            "sites"
    };

    private final DataSource dataSource;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final SiteService siteService;
    private final Map<String, String> rawSettings;

    @Autowired
    public SetupServiceDBM(DataSource dataSource, PasswordEncoder passwordEncoder,
                             UserService userService, SiteService siteService,
                             @Qualifier("rawSettingsMap") Map<String, String> rawSettings) {
        this.dataSource = dataSource;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.siteService = siteService;
        this.rawSettings = rawSettings;
    }

    @Override
    public boolean isRepositorySetup() throws SQLException {
        var tablesInDb = new ArrayList<String>();
        var resultSet = dataSource.getConnection().getMetaData().getTables(null, null, "%", null);
        while (resultSet.next()) {
            tablesInDb.add(resultSet.getString("TABLE_NAME"));
        }

        if(!userService.getUserByUsername("admin").isPresent())
        {
        	return false;
        }
        
    	for (var table : TABLES_NEEDED) {
            if (!tablesInDb.contains(table)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public void setupRepository() {
        var metaDataSrc = new MetadataSources(
                new StandardServiceRegistryBuilder().applySettings(rawSettings).build()
        );

        metaDataSrc.addAnnotatedClass(UserDB.class);
        metaDataSrc.addAnnotatedClass(SiteDB.class);
        metaDataSrc.addAnnotatedClass(PageDB.class);


        var schemaExport = new SchemaExport();
        schemaExport.setHaltOnError(false);
        schemaExport.setFormat(true);
        schemaExport.setDelimiter(";");
        schemaExport.execute(EnumSet.of(TargetType.DATABASE, TargetType.STDOUT), SchemaExport.Action.BOTH,
                metaDataSrc.buildMetadata());

        var admin = new User();
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("admin"));
        admin.setRoles(Arrays.asList(Role.values()));
        userService.create(admin);

        var site = Site.builder()
                .name("default")
                .path("/")
                .build();
        siteService.create(site);
    }
}
