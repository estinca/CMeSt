package com.est.repository.api.model;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Site {
	private String id;
	private String name;
	private String path;
	private Date createdAt;
	private Date updatedAt;
}
