package com.est.repository.dbm.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.est.repository.api.model.Site;
import com.est.repository.dbm.converter.SiteConverter;
import com.est.repository.dbm.dao.SiteDAO;
import com.est.repository.dbm.domain.SiteDB;

public class SiteServiceDBMTest {

	@Mock
	private SiteDAO siteDAO;
	
	@Mock
	private SiteConverter siteConverter;
	
	private SiteServiceDBM siteService;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		
		when(siteConverter.fromDB(any(SiteDB.class))).thenReturn(getSite());
		when(siteConverter.toDB(any(Site.class), anyBoolean())).thenReturn(getSiteDb());
		
		siteService = new SiteServiceDBM(siteDAO, siteConverter);
	}
	
	@Test
	public void getSites_success() {
		when(siteDAO.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(getSiteDbList()));

		Page<Site> page = siteService.getSites(PageRequest.of(1, 25));
		
		assertEquals(10, page.getTotalElements());
		verify(siteConverter, times(10)).fromDB(any(SiteDB.class));
	}
	
	@Test
	public void getSiteById_success() {
		when(siteDAO.findById(anyString())).thenReturn(Optional.of(getSiteDb()));
		
		Optional<Site> result = siteService.getSiteById("id");
		assertTrue(result.isPresent());
		verify(siteConverter).fromDB(any(SiteDB.class));
	}
	
	@Test
	public void getSiteById_empty() {
		when(siteDAO.findById(anyString())).thenReturn(Optional.empty());
		
		Optional<Site> result = siteService.getSiteById("id");
		assertFalse(result.isPresent());	
	}

	@Test
	public void create_success() {
		
	}
	
	
	private List<SiteDB> getSiteDbList() {
		List<SiteDB> list = new ArrayList<>();
		
		for (int i = 0; i < 10; i++) {
			list.add(getSiteDb());
			
		}
		
		return list;
	}
	
	private SiteDB getSiteDb() {
		SiteDB db = new SiteDB();
		
		db.setId("id");
		db.setName("name");
		db.setBasePath("/path/");
		db.setCreatedAt(new Date());
		db.setUpdatedAt(new Date());
		
		return db;
	}
	
	private Site getSite() {
		Site site = new Site();
		
		site.setId("id");
		site.setName("name");
		site.setBasePath("/path/");
		site.setCreatedAt(new Date());
		site.setUpdatedAt(new Date());
		
		return site;
	}
	
}
