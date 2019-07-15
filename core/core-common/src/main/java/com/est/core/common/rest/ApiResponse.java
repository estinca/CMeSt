package com.est.core.common.rest;

import org.springframework.data.domain.Page;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
public class ApiResponse {
	private Object data;
	
	@JsonInclude(Include.NON_NULL)
	private PageInfo pageInfo;
	
	public static ApiResponse createResponse(Object data) {
		ApiResponse apiResponse = new ApiResponse();
		
		if(data instanceof Page) {
			Page page = (Page) data;
			PageInfo pageInfo = new PageInfo();
			pageInfo.setTotalElements(page.getTotalElements());
			pageInfo.setTotalPages(page.getTotalPages());
			pageInfo.setSize(page.getSize());
			pageInfo.setElementsOnPage(page.getNumberOfElements());
			pageInfo.setFirst(page.isFirst());
			pageInfo.setLast(page.isLast());
			pageInfo.setSort(page.getSort());
			pageInfo.setPage(page.getNumber() + 1);
			
			apiResponse.setPageInfo(pageInfo);
			apiResponse.setData(data);
		} else {
			apiResponse.setData(data);
		}
		
		return apiResponse;
	}
}
