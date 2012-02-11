package de.benjaminborbe.tools.url;

import java.io.UnsupportedEncodingException;

public interface UrlUtil {

	String encode(String url) throws UnsupportedEncodingException;

	String decode(String url) throws UnsupportedEncodingException;

}
