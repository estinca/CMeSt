package com.est.repository.dbm.converter;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.est.repository.api.model.Page;
import com.est.repository.dbm.domain.PageDB;

import lombok.var;

@Component
public class PageConverter implements Converter<Page, PageDB> {

    private final SiteConverter siteConverter;

    @Autowired
    public PageConverter(SiteConverter siteConverter) {
        this.siteConverter = siteConverter;
    }

    @Override
    public PageDB toDB(Page page, boolean update) {
        var db = new PageDB();
        if (update) {
            db.setId(page.getId());
            db.setCreatedAt(page.getCreatedAt());
        }

        db.setName(page.getName());
        db.setTitle(page.getTitle());
        db.setPath(page.getPath());
        db.setPublished(page.isPublished());

        if (page.getParent() != null) {
            db.setParent(toDB(page.getParent(), true));
        }
        db.setSite(siteConverter.toDB(page.getSite(), true));
        db.setUpdatedAt(page.getUpdatedAt());

        return db;
    }

    @Override
    public Page fromDB(PageDB pageDB) {
        var page = new Page();

        page.setId(pageDB.getId());
        page.setName(pageDB.getName());
        page.setTitle(pageDB.getTitle());
        page.setPath(pageDB.getPath());
        page.setPublished(pageDB.isPublished());

        if (pageDB.getParent() != null) {
            page.setParent(fromDB(pageDB.getParent()));
        }

        page.setSite(siteConverter.fromDB(pageDB.getSite()));
        page.setCreatedAt(pageDB.getCreatedAt());
        page.setUpdatedAt(pageDB.getUpdatedAt());
        page.setChildren(new ArrayList<>());

        return page;
    }

    public Page fromDBWithChildren(PageDB pageDB) {
        var page = fromDB(pageDB);

        var children = new ArrayList<Page>();
        for (var child : pageDB.getChildren()) {
            children.add(fromDBWithChildren(child));
        }
        page.setChildren(children);

        return page;
    }
}
