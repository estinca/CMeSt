package com.est.repository.api.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.est.repository.api.model.Site;

public interface SiteService {
	
	Page<Site> getSites(Pageable pageable);
	Optional<Site> getSiteById(String id);
	Optional<Site> getSiteByName(String name);
	Site create(Site site);
	Site update(Site site);
	void delete(Site site);
	Optional<Site> getSiteByPath(String basePath);
}
