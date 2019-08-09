package com.est.core.common.rest.error;

import com.est.core.common.error.CoreError;

import lombok.Getter;

@Getter
public class ApiError {

    private int status;
    private String key;
    private Object detail;

    public ApiError(int statusCode, CoreError error) {
        this(statusCode, error, null);
    }

    public ApiError(int statusCode, CoreError error, Object detail) {
        status = statusCode;
        this.key = error.getKey();
        this.detail = detail;
    }

}
