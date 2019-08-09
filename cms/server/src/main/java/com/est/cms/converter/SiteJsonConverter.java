package com.est.cms.converter;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.est.cms.dto.request.SiteRequest;
import com.est.cms.dto.response.SiteResponse;
import com.est.core.common.rest.JsonConverter;
import com.est.repository.api.model.Site;

@Component
public class SiteJsonConverter implements JsonConverter<Site, SiteRequest, SiteResponse>{

	@Override
	public Site fromJson(SiteRequest request) {
		Site site = new Site();
		site.setName(request.getName());
		site.setPath(request.getPath());
		
		return site;
	}

	@Override
	public SiteResponse toJson(Site site) {
		SiteResponse response = new SiteResponse();
		response.setId(site.getId());
		response.setName(site.getName());
		response.setPath(site.getPath());
		response.setCreatedAt(site.getCreatedAt());
		response.setUpdatedAt(site.getUpdatedAt());
		
		return response;
	}

	@Override
	public Page<SiteResponse> toJson(Page<Site> sites) {
		return sites.map(this::toJson);
	}

}
