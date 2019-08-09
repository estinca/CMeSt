package com.est.cms.exception;

import com.est.core.common.error.CoreError;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CMSError implements CoreError {

    PAGE_PARENT_NOT_FOUND("page_parent_not_found");

    private String key;
}
