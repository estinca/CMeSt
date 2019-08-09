package com.est.cms.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.est.cms.dto.response.TreeItemResponse;
import com.est.cms.service.TreeViewService;
import com.est.repository.api.exception.BrakingRepositoryException;
import com.est.repository.api.model.Page;
import com.est.repository.api.model.Site;
import com.est.repository.api.service.PageService;

import lombok.var;

@Service
public class TreeViewServiceImpl implements TreeViewService {

    private final PageService pageService;

    @Autowired
    public TreeViewServiceImpl(PageService pageService) {
        this.pageService = pageService;
    }

    @Override
    public TreeItemResponse getTreeViewBySite(Site site) throws BrakingRepositoryException {
        var page = pageService.getRootPageForSite(site);

        var tree = new TreeItemResponse(page.getName(), page.getId());
        tree.setChildren(getChildren(page.getChildren()));

        orderTreeLevel(tree.getChildren());
        return tree;
    }

    private List<TreeItemResponse> getChildren(List<Page> pages) {
        var children = new ArrayList<TreeItemResponse>();
        for (var page : pages) {
            var child = new TreeItemResponse(page.getName(), page.getId());
            child.setChildren(getChildren(page.getChildren()));
            children.add(child);
        }

        return children;
    }

    private void orderTreeLevel(List<TreeItemResponse> treeLevel) {
        for (var item : treeLevel) {
            orderTreeLevel(item.getChildren());
        }

        treeLevel.sort((o1, o2) -> o1.getName().compareToIgnoreCase(o2.getName()));
    }

}
