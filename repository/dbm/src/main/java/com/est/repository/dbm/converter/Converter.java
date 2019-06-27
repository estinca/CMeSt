package com.est.repository.dbm.converter;

public interface Converter<API, DB> {

    DB toDB(API entity, boolean update);

    API fromDB(DB db);
}
