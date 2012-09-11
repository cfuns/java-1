package de.benjaminborbe.tools.url;

import java.io.UnsupportedEncodingException;
import java.util.Map;

public interface UrlUtil {

	String encode(String url) throws UnsupportedEncodingException;

	String decode(String url) throws UnsupportedEncodingException;

	String buildUrl(String path, Map<String, String> parameter) throws UnsupportedEncodingException;
}
