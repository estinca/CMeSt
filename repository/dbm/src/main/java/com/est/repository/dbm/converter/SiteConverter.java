package com.est.repository.dbm.converter;

import org.springframework.stereotype.Component;

import com.est.repository.api.model.Site;
import com.est.repository.dbm.domain.SiteDB;

@Component
public class SiteConverter implements Converter<Site, SiteDB> {

	@Override
	public SiteDB toDB(Site entity, boolean update) {
		SiteDB db = new SiteDB();
		
		if(update) {
			db.setId(entity.getId());
			db.setCreatedAt(entity.getCreatedAt());
		}
		db.setName(entity.getName());
		db.setPath(entity.getPath());
		db.setUpdatedAt(entity.getUpdatedAt());
		
		return db;
	}

	@Override
	public Site fromDB(SiteDB db) {
		Site entity = new Site();
		
		entity.setId(db.getId());
		entity.setName(db.getName());
		entity.setPath(db.getPath());
		entity.setCreatedAt(db.getCreatedAt());
		entity.setUpdatedAt(db.getUpdatedAt());
		
		return entity;
	}
	
}
