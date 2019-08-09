package com.est.repository.api.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.est.repository.api.model.Site;

public interface SiteService {

    Site create(Site site);

    Site update(Site oldSite, Site newSite);

    Optional<Site> getSiteById(String id);

    Page<Site> getSites(Pageable pageable);

    void delete(Site site);
}
