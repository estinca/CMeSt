package com.est.cms.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import lombok.Data;

@Data
public class SiteRequest {

	@NotBlank(message = "site.name.blank")
	@Length(min = 1, max = 255, message = "site.name.length")
	private String name;

	@NotBlank(message = "site.path.blank")
	@Pattern(regexp = "^[a-zA-Z0-9_/]*$", message="page.path.invalid")
	private String path;
}
