package com.est.repository.dbm.converter;

import org.springframework.stereotype.Component;

import com.est.repository.api.model.User;
import com.est.repository.dbm.domain.UserDB;

@Component
public class UserConverterRDBMS  implements Converter<User, UserDB> {


    @Override
    public UserDB toDB(User entity, boolean update) {
        UserDB db = new UserDB();

        if(update) {
            db.setId(entity.getId());
            db.setCreatedAt(entity.getCreatedAt());
        }

        db.setUsername(entity.getUsername());
        db.setPassword(entity.getPassword());
        db.setRoles(entity.getRoles());

        return db;
    }

    @Override
    public User fromDB(UserDB userDB) {
       User entity = new User();

       entity.setId(userDB.getId());
       entity.setUsername(userDB.getUsername());
       entity.setPassword(userDB.getPassword());
       entity.setRoles(userDB.getRoles());
       entity.setCreatedAt(userDB.getCreatedAt());
       entity.setUpdatedAt(userDB.getUpdatedAt());

       return entity;
    }
}
