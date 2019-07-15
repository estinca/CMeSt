package com.est.core.common.rest;

import lombok.Data;

import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
public class ApiError {
	private final boolean error = true;
	private int statusCode;
	private String title;
	private Object details;
}
