package com.est.core.common.rest.error;

import com.est.core.common.error.CoreError;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ApiErrorType implements CoreError {

    NOT_FOUND("not_found"),
    INTERNAL_ERROR("internal_error"),
    ACCESS_DENIED("access_denied"),
    EXPIRED_TOKEN("expired_token"),
    VALIDATION_ERROR("validation_error");

    private String key;

}
