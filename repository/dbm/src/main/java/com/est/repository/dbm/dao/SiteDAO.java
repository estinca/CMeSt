package com.est.repository.dbm.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.est.repository.dbm.domain.SiteDB;

public interface SiteDAO extends JpaRepository<SiteDB, String> {
	Optional<SiteDB> findByName(String name);

	Optional<SiteDB> findByBasePath(String basePath);
}
