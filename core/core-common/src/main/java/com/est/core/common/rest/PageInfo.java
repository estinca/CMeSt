package com.est.core.common.rest;

import org.springframework.data.domain.Sort;

import lombok.Data;

@Data
public class PageInfo {
	private long totalElements;
	private long totalPages;
	private int size;
	private int page;
	private int elementsOnPage;
	private Sort sort;
	private boolean last;
	private boolean first;
}
