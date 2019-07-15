package com.est.utils;

import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;

public class UrlUtilsTest {
	
	
	@Test
	public void removeTrailingSlash_withEndingSlash() {
		assertEquals("/test", UrlUtils.removeTrailingSlash("/test/"));
	}
	
	@Test
	public void removeTrailingSlash_withoutEndingSlash() {
		assertEquals("/test", UrlUtils.removeTrailingSlash("/test"));
	}
	
	@Test
	public void addBeginningSlash_withBeginningSlash() {
		assertEquals("/test", UrlUtils.addBeginningSlash("/test"));
	}
	
	@Test
	public void addBeginningSlash_withoutBeginningSlash() {
		assertEquals("/test", UrlUtils.addBeginningSlash("test"));
	}
	
}
