package com.est.repository.dbm.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.est.repository.api.model.Site;
import com.est.repository.api.service.SiteService;
import com.est.repository.dbm.converter.SiteConverter;
import com.est.repository.dbm.dao.SiteDAO;
import com.est.utils.UrlUtils;

@Service
public class SiteServiceDBM implements SiteService{

	private SiteDAO siteDAO;
	private SiteConverter siteConverter;

	@Autowired
	public SiteServiceDBM(SiteDAO siteDAO, SiteConverter siteConverter) {
		this.siteDAO = siteDAO;
		this.siteConverter = siteConverter;
	}
	
	@Override
	public Page<Site> getSites(Pageable pageable) {
		return siteDAO.findAll(pageable).map(siteConverter::fromDB);
	}

	@Override
	public Optional<Site> getSiteById(String id) {
		return siteDAO.findById(id).map(siteConverter::fromDB);
	}

	@Override
	public Optional<Site> getSiteByPath(String basePath) {
		return siteDAO.findByBasePath(basePath).map(siteConverter::fromDB);
	}
	
	@Override
	public Optional<Site> getSiteByName(String name) {
		return siteDAO.findByName(name).map(siteConverter::fromDB);
	}
	
	@Override
	public Site create(Site site) {
		site.setBasePath(UrlUtils.optimizeUrl(site.getBasePath()));
		return siteConverter.fromDB(siteDAO.saveAndFlush(siteConverter.toDB(site, false)));
	}

	@Override
	public Site update(Site site) {
		site.setBasePath(UrlUtils.optimizeUrl(site.getBasePath()));
		return siteConverter.fromDB(siteDAO.saveAndFlush(siteConverter.toDB(site, true)));
	}

	@Override
	public void delete(Site site) {
		siteDAO.delete(siteConverter.toDB(site, true));
		
	}


}
