package de.benjaminborbe.httpdownloader.api;

import java.net.URL;

public interface HttpResponse {

	URL getUrl();

	Long getDuration();

	HttpHeader getHeader();

	HttpContent getContent();
}
