package com.est.cms.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.est.cms.dto.request.PageRequest;
import com.est.cms.dto.response.PageResponse;
import com.est.cms.exception.CMSError;
import com.est.cms.exception.CMSException;
import com.est.repository.api.model.Page;
import com.est.repository.api.service.PageService;

import lombok.var;

@Component
public class PageJsonConverter {

    private final SiteJsonConverter siteConverter;
    private final PageService pageService;

    @Autowired
    public PageJsonConverter(SiteJsonConverter siteConverter, PageService pageService) {
        this.siteConverter = siteConverter;
        this.pageService = pageService;
    }

    public Page fromJson(PageRequest request) {

        var parent = pageService.getPageById(request.getParent());

        var page = new Page();
        page.setName(request.getName());
        page.setTitle(request.getTitle());
        page.setParent(parent.orElseThrow(() -> new CMSException(CMSError.PAGE_PARENT_NOT_FOUND)));

        return page;
    }

    public PageResponse toJson(Page page) {
        var res = toJsonForDetail(page);

        if (page.getParent() != null) {
            res.setParent(toJsonForDetail(page.getParent()));
        }

        return res;
    }

    public org.springframework.data.domain.Page<PageResponse> toJson(org.springframework.data.domain.Page<Page> pages) {
        return pages.map(this::toJsonForList);
    }

    private PageResponse toJsonForDetail(Page page) {
        return PageResponse.builder()
                .id(page.getId())
                .name(page.getName())
                .title(page.getTitle())
                .path(page.getPath())
                .published(page.isPublished())
                .site(siteConverter.toJson(page.getSite()))
                .createdAt(page.getCreatedAt())
                .updatedAt(page.getUpdatedAt())
                .build();
    }

    private PageResponse toJsonForList(Page page) {
        return PageResponse.builder()
                .id(page.getId())
                .name(page.getName())
                .title(page.getTitle())
                .path(page.getPath())
                .published(page.isPublished())
                .site(null)
                .parent(null)
                .createdAt(page.getCreatedAt())
                .updatedAt(page.getUpdatedAt())
                .build();
    }
}