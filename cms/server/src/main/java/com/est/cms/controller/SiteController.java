package com.est.cms.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.est.cms.converter.SiteJsonConverter;
import com.est.cms.dto.request.SiteRequest;
import com.est.core.common.rest.ApiResponse;
import com.est.core.common.rest.exception.ApiException;
import com.est.repository.api.model.Site;
import com.est.repository.api.service.SiteService;
import com.est.utils.UrlUtils;

import javassist.NotFoundException;
@RestController
@RequestMapping("/sites")
public class SiteController {
	
	private SiteService siteService;
	private SiteJsonConverter siteJsonConverter;
	
	
	@Autowired
	public SiteController(SiteService siteService, SiteJsonConverter siteJsonConverter) {
		this.siteService = siteService;
		this.siteJsonConverter = siteJsonConverter;
	}
	
	
	@PostMapping
	public ResponseEntity<ApiResponse> createSite(@RequestBody @Valid SiteRequest siteRequest) {
		Site site = siteJsonConverter.fromJson(siteRequest);
		
		
		if(siteService.getSiteByName(site.getName()).isPresent()) {
			throw new ApiException("site.name.not-unique", "Site already exists.");
		}

		site.setBasePath(UrlUtils.optimizeUrl(site.getBasePath()));
		if (siteService.getSiteByPath(site.getBasePath()).isPresent()) {
            throw new ApiException("site.path.not-unique", "Site path already exists.");
        }
		
		site = siteService.create(site);
		
		return new ResponseEntity<>(ApiResponse.createResponse(siteJsonConverter.toJson(site)), HttpStatus.CREATED);
	}
	
	
	@GetMapping
	public ResponseEntity<ApiResponse> getSites(Pageable pageable,
			@RequestParam(value = "paginated", required= false, defaultValue = "true") boolean paginated) {
		if(!paginated) {
			pageable = PageRequest.of(0, Integer.MAX_VALUE);
		}
		
		return new ResponseEntity<>(ApiResponse.createResponse(
				siteJsonConverter.toJson(siteService.getSites(pageable))), HttpStatus.OK);
	}
	
	
	@GetMapping("/{id:[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}}")
	public ResponseEntity<ApiResponse> getSite(@PathVariable String id) throws NotFoundException {
		return new ResponseEntity<>(ApiResponse.createResponse(siteJsonConverter.toJson(getSiteById(id))),  HttpStatus.OK);
	}
	
	
	@DeleteMapping("/{id:[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}}")
	public ResponseEntity<ApiResponse> deleteSite(@PathVariable String id) throws NotFoundException {
		
		siteService.getSiteById(id).ifPresent(siteService::delete);
		return new ResponseEntity<>(ApiResponse.createResponse(null),  HttpStatus.OK);
	}
	
	
	@PutMapping("/{id:[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}}")
	public ResponseEntity<ApiResponse> updateSite(@PathVariable String id, @RequestBody @Valid SiteRequest request) throws NotFoundException {
		
		Site oldSite = getSiteById(id);
		Site newSite = siteJsonConverter.fromJson(request);
		
		if(!oldSite.getName().equalsIgnoreCase(newSite.getName()) 
				&& siteService.getSiteByName(newSite.getName()).isPresent()) {
			throw new ApiException("site.name.not-unique", "Site already exists");
		}
		if(!oldSite.getBasePath().equalsIgnoreCase(newSite.getBasePath()) 
				&& siteService.getSiteByPath(newSite.getBasePath()).isPresent()) {
			throw new ApiException("site.path.not-unique", "Site path already exists");
		}

		newSite.setId(oldSite.getId());
		newSite.setCreatedAt(oldSite.getCreatedAt());
		
		newSite = siteService.update(newSite);
		
		return new ResponseEntity<>(ApiResponse.createResponse(siteJsonConverter.toJson(newSite)),  HttpStatus.OK);
	}
	
	
	private Site getSiteById(String id) throws NotFoundException {
		
		return siteService.getSiteById(id)
				.orElseThrow(() -> new NotFoundException("site with id: " + id + "not found"));
	}
}
