package com.est.repository.dbm.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.est.repository.api.exception.BrakingRepositoryException;
import com.est.repository.api.exception.RepositoryError;
import com.est.repository.api.exception.RepositoryException;
import com.est.repository.api.model.Page;
import com.est.repository.api.model.Site;
import com.est.repository.api.service.PageService;
import com.est.repository.dbm.converter.PageConverter;
import com.est.repository.dbm.converter.SiteConverter;
import com.est.repository.dbm.dao.PageDAO;
import com.est.repository.dbm.domain.PageDB;
import com.est.utils.UrlUtils;

import lombok.var;

@Service
@Transactional
public class PageServiceDBM implements PageService {

    private final PageDAO repository;
    private final PageConverter converter;
    private final SiteConverter siteConverter;

    @Autowired
    public PageServiceDBM(PageDAO repository, PageConverter pageConverter, SiteConverter siteConverter) {
        this.repository = repository;
        this.converter = pageConverter;
        this.siteConverter = siteConverter;
    }

    @Override
    public Optional<Page> getPageById(String id) {
        return repository.findById(id).map(converter::fromDBWithChildren);
    }

    @Override
    public Page create(Page page) {
        // TODO create root Node for page

        // TODO REFACTOR to something simpler
        if (page.getParent() != null &&
                page.getParent().getChildren().stream().anyMatch(child -> child.getName().equals(page.getName()))) {
            throw new RepositoryException(RepositoryError.PAGE_NAME_NOT_UNIQUE);
        }

        var basePath = page.getParent() != null ? UrlUtils.addTrailingSlash(page.getParent().getPath()) : "/";
        page.setPath(UrlUtils.optimizeUrl(basePath + page.getName()));

        return converter.fromDB(repository.saveAndFlush(converter.toDB(page, false)));
    }

    @Override
    public Page update(Page oldPage, Page newPage) {
        newPage.setSite(oldPage.getSite());
        newPage.setId(oldPage.getId());
        newPage.setChildren(oldPage.getChildren());
        newPage.setPublished(oldPage.isPublished());
        newPage.setCreatedAt(oldPage.getCreatedAt());

        // TODO REFACTOR to something simpler
        if (newPage.getParent() != null && !newPage.getName().equals(oldPage.getName()) &&
                newPage.getParent().getChildren().stream().anyMatch(child -> child.getName().equals(newPage.getName()))) {
            throw new RepositoryException(RepositoryError.PAGE_NAME_NOT_UNIQUE);
        }

        var basePath = newPage.getParent() != null ? UrlUtils.addTrailingSlash(newPage.getParent().getPath()) : "/";
        newPage.setPath(UrlUtils.optimizeUrl(basePath + newPage.getName()));

        return converter.fromDB(repository.saveAndFlush(converter.toDB(newPage, true)));
    }

    @Override
    public void delete(Page page) {
        repository.delete(converter.toDB(page, true));
    }

    @Override
    public Optional<Page> getPageByName(String name) {
        return repository.findByName(name).map(converter::fromDB);
    }
    
    @Override
    public Optional<Page> getPageByPath(String path) {
        return repository.findByPath(path).map(converter::fromDB);
    }

    @Override
    public List<Page> getPagesBySite(Site site) {
        return repository.findBySite(siteConverter.toDB(site, true))
                .stream()
                .map(converter::fromDB)
                .collect(Collectors.toList());
    }

    @Override
    public org.springframework.data.domain.Page<Page> getPagesBySite(Site site, Pageable pageable) {
        return repository.findBySite(siteConverter.toDB(site, true), pageable)
                .map(converter::fromDB);
    }

    @Override
    public org.springframework.data.domain.Page<Page> getPagesBySiteAndParent(Site site, Page parent, boolean includeParent, Pageable pageable) {
        var siteDB = siteConverter.toDB(site, true);
        var parentDB = converter.toDB(parent, true);
        org.springframework.data.domain.Page<PageDB> pages;

        if (includeParent) {
            pages = repository.findBySiteAndParentOrderByNameAscIncludeParent(siteDB, parentDB, parentDB.getId(), pageable);
        } else {
            pages = repository.findBySiteAndParentOrderByNameAsc(siteDB, parentDB, pageable);
        }

        return pages.map(converter::fromDB);
    }

    @Override
    public Page getRootPageForSite(Site site) throws BrakingRepositoryException {
        return repository.findBySiteAndParentIsNull(siteConverter.toDB(site, true))
                .map(converter::fromDBWithChildren)
                .orElseThrow(() -> new BrakingRepositoryException("This site does not have a root. This should not be possible"));
    }
}
