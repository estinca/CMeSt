package com.est.cms.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.est.cms.converter.SiteJsonConverter;
import com.est.cms.dto.request.SiteRequest;
import com.est.cms.dto.response.SiteResponse;
import com.est.core.common.rest.ApiResponse;
import com.est.core.common.rest.pagination.PaginatedResponse;
import com.est.repository.api.model.Site;
import com.est.repository.api.service.SiteService;

import javassist.NotFoundException;
import lombok.var;

@RestController
@RequestMapping("/sites")
public class SiteController {
	
    private final SiteJsonConverter jsonConverter;
    private final SiteService siteService;

    @Autowired
    public SiteController(SiteJsonConverter jsonConverter, SiteService siteService) {
        this.jsonConverter = jsonConverter;
        this.siteService = siteService;
    }

//    @Secured("ROLE_ADMIN")
    @PostMapping
    public ResponseEntity<SiteResponse> createSite(@RequestBody @Valid SiteRequest request) {
        var site = siteService.create(jsonConverter.fromJson(request));
        return new ResponseEntity<>(jsonConverter.toJson(site), HttpStatus.CREATED);
    }

//    @Secured("ROLE_ADMIN")
    @GetMapping
    public ResponseEntity<PaginatedResponse<SiteResponse>> getSites(Pageable pageable) {
        var sites = jsonConverter.toJson(siteService.getSites(pageable));
        return new ResponseEntity<>(PaginatedResponse.createResponse(sites, "/sites"), HttpStatus.OK);
    }

    @RequestMapping(value = "/{id:[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}}",
            method = RequestMethod.GET)
    public ResponseEntity<SiteResponse> getSite(@PathVariable String id) throws NotFoundException {
        return new ResponseEntity<>(jsonConverter.toJson(getSiteById(id)), HttpStatus.OK);
    }

    @RequestMapping(value = "/{id:[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}}",
            method = RequestMethod.PUT)
    public ResponseEntity<SiteResponse> updateSite(@PathVariable String id,
                                                   @RequestBody @Valid SiteRequest request) throws NotFoundException {
        var oldSite = getSiteById(id);
        var newSite = jsonConverter.fromJson(request);
        var site = siteService.update(oldSite, newSite);
        return new ResponseEntity<>(jsonConverter.toJson(site), HttpStatus.OK);
    }

    @RequestMapping(value = "/{id:[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}}",
            method = RequestMethod.DELETE)
    public ResponseEntity<Void> delteSite(@PathVariable String id) {
        siteService.getSiteById(id).ifPresent(siteService::delete);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private Site getSiteById(String id) throws NotFoundException {
        return siteService.getSiteById(id)
                .orElseThrow(() -> new NotFoundException("Could not find a site with the id: '" + id + "'"));
}
}
