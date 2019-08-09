package com.est.cms.service;

import com.est.cms.dto.response.TreeItemResponse;
import com.est.repository.api.exception.BrakingRepositoryException;
import com.est.repository.api.model.Site;

public interface TreeViewService {
    TreeItemResponse getTreeViewBySite(Site site) throws BrakingRepositoryException;
}