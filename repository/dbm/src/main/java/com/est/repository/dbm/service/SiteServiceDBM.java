package com.est.repository.dbm.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.est.repository.api.exception.RepositoryError;
import com.est.repository.api.exception.RepositoryException;
import com.est.repository.api.model.Site;
import com.est.repository.api.service.PageService;
import com.est.repository.api.service.SiteService;
import com.est.repository.dbm.converter.SiteConverter;
import com.est.repository.dbm.dao.SiteDAO;
import com.est.utils.UrlUtils;

import lombok.var;

@Service
public class SiteServiceDBM implements SiteService{


    private final SiteDAO repository;
    private final SiteConverter converter;
    private final PageService pageService;

    @Autowired
    public SiteServiceDBM(SiteDAO repository, SiteConverter converter, PageService pageService) {
        this.repository = repository;
        this.converter = converter;
        this.pageService = pageService;
    }

    @Override
    public Site create(Site site) {
        if (repository.findByName(site.getName()).isPresent()) {
            throw new RepositoryException(RepositoryError.SITE_NAME_NOT_UNIQUE);
        }

        if (repository.findByPath(site.getPath()).isPresent()) {
            throw new RepositoryException(RepositoryError.SITE_PATH_NOT_UNIQUE);
        }

        if(pageService.getPageByPath(UrlUtils.optimizeUrl(site.getPath())).isPresent()) {
        	throw new RepositoryException(RepositoryError.PATH_NOT_UNIQUE);
        }
        site = converter.fromDB(repository.saveAndFlush(converter.toDB(site, false)));

        var page = new com.est.repository.api.model.Page();
        page.setName(site.getName());
        page.setTitle(site.getName());
        page.setSite(site);

        pageService.create(page);

        return site;
    }

    @Override
    public Site update(Site oldSite, Site newSite) {

        if (!oldSite.getName().equalsIgnoreCase(newSite.getName()) && repository.findByName(newSite.getName()).isPresent()) {
            throw new RepositoryException(RepositoryError.SITE_NAME_NOT_UNIQUE);
        }

        if (!oldSite.getPath().equalsIgnoreCase(newSite.getPath()) && repository.findByPath(newSite.getPath()).isPresent()) {
            throw new RepositoryException(RepositoryError.SITE_PATH_NOT_UNIQUE);
        }

        if(pageService.getPageByPath(UrlUtils.optimizeUrl(newSite.getPath())).isPresent()) {
        	throw new RepositoryException(RepositoryError.PATH_NOT_UNIQUE);
        }
        
        newSite.setId(oldSite.getId());
        newSite.setCreatedAt(oldSite.getCreatedAt());

        return converter.fromDB(repository.saveAndFlush(converter.toDB(newSite, true)));
    }

    @Override
    public Optional<Site> getSiteById(String id) {
        return repository.findById(id).map(converter::fromDB);
    }

    @Override
    public Page<Site> getSites(Pageable pageable) {
        return repository.findAll(pageable).map(converter::fromDB);
    }

    @Override
    public void delete(Site site) {
        repository.deleteById(site.getId());
    }

}