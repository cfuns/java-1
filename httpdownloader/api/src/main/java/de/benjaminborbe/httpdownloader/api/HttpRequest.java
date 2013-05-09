package de.benjaminborbe.httpdownloader.api;

import java.net.URL;
import java.util.Map;

public interface HttpRequest {

	HttpMethod getHttpMethod();

	HttpHeader getHeader();

	URL getUrl();

	Integer getTimeout();

	Boolean getSecure();

	Map<String, String> getParameter();

	String getUsername();

	String getPassword();

}
