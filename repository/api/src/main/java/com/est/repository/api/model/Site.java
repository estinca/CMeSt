package com.est.repository.api.model;

import java.util.Date;

import lombok.Data;

@Data
public class Site {
	private String id;
	private String name;
	private String basePath;
	private Date createdAt;
	private Date updatedAt;
}
