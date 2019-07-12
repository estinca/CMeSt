package com.est.repository.dbm.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.est.repository.dbm.domain.SiteDB;

public interface SiteDAO extends JpaRepository<SiteDB, String> {

}
