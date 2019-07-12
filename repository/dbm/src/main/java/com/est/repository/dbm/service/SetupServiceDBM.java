package com.est.repository.dbm.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.Map;
import java.util.Optional;

import javax.sql.DataSource;

import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.hibernate.tool.schema.TargetType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.est.repository.api.exception.RepositoryException;
import com.est.repository.api.model.Role;
import com.est.repository.api.model.User;
import com.est.repository.api.service.SetupService;

@Service
public class SetupServiceDBM implements SetupService {

    private static final String[] TABLES_NEEDED = {
		"users", 
		"user_roles",
		"sites"
    };
    
	private final DataSource dataSource;
    private final PasswordEncoder passwordEncoder;
    private final UserServiceDBM userService;
    private final Map<String, String> rawSettings;

    @Autowired
    public SetupServiceDBM(DataSource dataSource, PasswordEncoder passwordEncoder, UserServiceDBM userService,
    		@Qualifier("rawSettingsMap") Map<String, String> rawSettings) {
        this.dataSource = dataSource;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.rawSettings = rawSettings;
    }

    @Override
    public boolean isRepositorySetup() throws RepositoryException {
        ArrayList<String> tablesInDb = new ArrayList<>();
        try {
            ResultSet resultSet = dataSource.getConnection().getMetaData().getTables(null, null, "%", null);
            while (resultSet.next()) {
				tablesInDb.add(resultSet.getString("TABLE_NAME"));
			}
            
            for(String table: TABLES_NEEDED) {
            	if(!tablesInDb.contains(table)) {
            		return false;
            	}
            }
            
            Optional<User> admin = userService.getUserByUsername("admin");
            if(!admin.isPresent() || !admin.get().getRoles().contains(Role.ADMIN)) {
            	return false;
            } 

        } catch(SQLException e) {
            throw new RepositoryException("Something wrong in result set", e);
        }
        
        return true;
    }

    @Override
    public void setupRepository() {
    	MetadataSources metadataSources = new MetadataSources(
    			new StandardServiceRegistryBuilder().applySettings(rawSettings).build());
    	
    	metadataSources.addAnnotatedClass(User.class);
    	
    	SchemaExport schemaExport = new SchemaExport();
    	schemaExport.setHaltOnError(false);
    	schemaExport.setFormat(true);
    	schemaExport.setDelimiter(";");
    	schemaExport.execute(EnumSet.of(TargetType.DATABASE, TargetType.STDOUT), SchemaExport.Action.BOTH, metadataSources.buildMetadata());
    	
    	User admin = new User();
    	admin.setUsername("admin");
    	admin.setPassword(passwordEncoder.encode("admin"));
    	admin.setRoles(Arrays.asList(Role.values()));
    	userService.create(admin);
    	
    	//TODO create site object
    }
}
