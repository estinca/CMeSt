package com.est.utils;

public class UrlUtils {
	private UrlUtils() {
	}
	
	public static String removeTrailingSlash(String url) {
		if(url.length() > 1 && url.endsWith("/")) {
			return url.substring(0, url.length() - 1);
		}
		return url;
	}
	
	public static String addBeginningSlash(String url) {
		if(! url.startsWith("/")) {
			return "/" + url;
		}
		return url;
	}
	
	public static String optimizeUrl(String url) {
		return removeTrailingSlash(addBeginningSlash(url));
	}
	
}
