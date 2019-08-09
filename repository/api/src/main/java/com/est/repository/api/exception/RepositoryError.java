package com.est.repository.api.exception;

import com.est.core.common.error.CoreError;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RepositoryError implements CoreError {

    SITE_NAME_NOT_UNIQUE("site_name_not_unique"),
    SITE_PATH_NOT_UNIQUE("site_path_not_unique"),
    PAGE_NAME_NOT_UNIQUE("page_name_not_unique");

    private String key;
}
