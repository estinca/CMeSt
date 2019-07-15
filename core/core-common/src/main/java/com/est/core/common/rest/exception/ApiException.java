package com.est.core.common.rest.exception;

import lombok.Getter;

public class ApiException extends RuntimeException{
	
	@Getter
	private String title;
	
	public ApiException(String title, String details) {
		super(details);
		this.title = title;
	}
}
