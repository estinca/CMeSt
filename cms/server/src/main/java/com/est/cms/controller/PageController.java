package com.est.cms.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.est.cms.converter.PageJsonConverter;
import com.est.cms.dto.request.PageRequest;
import com.est.cms.dto.response.PageResponse;
import com.est.cms.dto.response.TreeItemResponse;
import com.est.cms.service.TreeViewService;
import com.est.core.common.rest.error.NotFoundException;
import com.est.core.common.rest.pagination.PaginatedResponse;
import com.est.repository.api.exception.BrakingRepositoryException;
import com.est.repository.api.model.Page;
import com.est.repository.api.model.Site;
import com.est.repository.api.service.PageService;
import com.est.repository.api.service.SiteService;

import lombok.var;


@RestController
@RequestMapping("/sites/{siteId:[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}}")
public class PageController {

    private final PageService pageService;
    private final PageJsonConverter pageConverter;
    private final SiteService siteService;
    private final TreeViewService treeViewService;

    @Autowired
    public PageController(PageService pageService, PageJsonConverter pageConverter,
                          SiteService siteService, TreeViewService treeViewService) {
        this.pageService = pageService;
        this.pageConverter = pageConverter;
        this.siteService = siteService;
        this.treeViewService = treeViewService;
    }

    @RequestMapping(value = "/page-tree", method = RequestMethod.GET)
    public ResponseEntity<TreeItemResponse> getSitePageTree(@PathVariable String siteId) throws BrakingRepositoryException {
        Site site = getSiteById(siteId);

        return new ResponseEntity<>(treeViewService.getTreeViewBySite(site), HttpStatus.OK);
    }


    @RequestMapping(value = "/pages", method = RequestMethod.GET)
    public ResponseEntity<PaginatedResponse<PageResponse>> getPages(
            Pageable pageable, @PathVariable String siteId,
            @RequestParam(value = "parent", required = false, defaultValue = "") String parentId,
            @RequestParam(value = "includeParent", required = false, defaultValue = "false") boolean includeParent
    ) {

        var site = getSiteById(siteId);
        org.springframework.data.domain.Page<Page> pages;
        if (parentId.isEmpty()) {
            pages = pageService.getPagesBySite(site, pageable);
        } else {
            var parent = pageService.getPageById(parentId)
                    .orElseThrow(() -> new NotFoundException("Could not find a page with id '" + parentId + "'"));
            pages = pageService.getPagesBySiteAndParent(site, parent, includeParent, pageable);
        }

        var baseUrl = "/sites/" + siteId + "/pages";
        return new ResponseEntity<>(PaginatedResponse.createResponse(pageConverter.toJson(pages), baseUrl), HttpStatus.OK);
    }

    @RequestMapping(value = "/pages", method = RequestMethod.POST)
    public ResponseEntity<PageResponse> createPage(@PathVariable String siteId, @RequestBody @Valid PageRequest request) {
        var site = getSiteById(siteId);
        var page = pageConverter.fromJson(request);
        page.setSite(site);

        page = pageService.create(page);

        return new ResponseEntity<>(pageConverter.toJson(page), HttpStatus.CREATED);
    }

    @RequestMapping(value = "/pages/{id}", method = RequestMethod.GET)
    public ResponseEntity<PageResponse> getPageById(@PathVariable String siteId, @PathVariable String id) {
        getSiteById(siteId);
        var page = getPageById(id);

        return new ResponseEntity<>(pageConverter.toJson(page), HttpStatus.OK);
    }

    @RequestMapping(value = "/pages/{id}", method = RequestMethod.PUT)
    public ResponseEntity<PageResponse> createPage(@PathVariable String siteId, @PathVariable String id,
                                                   @RequestBody @Valid PageRequest request) {
        getSiteById(siteId);
        var oldPage = getPageById(id);

        var newPage = pageConverter.fromJson(request);
        newPage = pageService.update(oldPage, newPage);

        return new ResponseEntity<>(pageConverter.toJson(newPage), HttpStatus.OK);
    }
    
    @RequestMapping(value = "pages/{id}",
            method = RequestMethod.DELETE)
    public ResponseEntity<Void> deletePage(@PathVariable String id) {
        pageService.getPageById(id).ifPresent(pageService::delete);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private Site getSiteById(String id) {
        return siteService.getSiteById(id).
                orElseThrow(() -> new NotFoundException("Could not find a site with the id: '" + id + "'"));
    }

    private Page getPageById(String id) {
        return pageService.getPageById(id).
                orElseThrow(() -> new NotFoundException("Could not find a page with the id: '" + id + "'"));
    }
}