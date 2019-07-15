package com.est.cms.dto.response;

import java.util.Date;

import lombok.Data;

@Data
public class SiteResponse {
	private String id;
	private String name;
	private String path;
	private Date createdAt;
	private Date updatedAt;
}
