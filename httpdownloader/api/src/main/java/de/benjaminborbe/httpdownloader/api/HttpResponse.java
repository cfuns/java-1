package de.benjaminborbe.httpdownloader.api;

import java.net.URL;

public interface HttpResponse {

	Integer getReturnCode();

	URL getUrl();

	Long getDuration();

	HttpHeader getHeader();

	HttpContent getContent();
}
