package com.est.repository.dbm.service.stubs;

import java.util.ArrayList;
import java.util.List;

import com.est.repository.api.model.Site;

public class SiteStubLoader {
	public static List<Site> loadSites() {
		List<Site> sites = new ArrayList<Site>();
		
		Site shop = new Site();
		shop.setName("shop");
		shop.setBasePath("/shop");
		
		sites.add(shop);
		
		
		Site blog = new Site();
		blog.setName("blog");
		blog.setBasePath("/blog");
		
		sites.add(blog);
		
		return sites;
	}
}
