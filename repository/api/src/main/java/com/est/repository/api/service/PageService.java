package com.est.repository.api.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;

import com.est.repository.api.exception.BrakingRepositoryException;
import com.est.repository.api.model.Page;
import com.est.repository.api.model.Site;

public interface PageService {

    Optional<Page> getPageById(String id);

    Page create(Page page);

    Page update(Page oldPage, Page newPage);

    void delete(Page page);

    Optional<Page> getPageByName(String name);

    Optional<Page> getPageByPath(String path);

    List<Page> getPagesBySite(Site site);

    org.springframework.data.domain.Page<Page> getPagesBySiteAndParent(Site site, Page parent,
                                                                       boolean includeParent, Pageable pageable);

    Page getRootPageForSite(Site site) throws BrakingRepositoryException;

    org.springframework.data.domain.Page<Page> getPagesBySite(Site site, Pageable pageable);

}