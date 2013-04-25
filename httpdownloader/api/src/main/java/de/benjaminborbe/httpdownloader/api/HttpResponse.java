package de.benjaminborbe.httpdownloader.api;

import java.net.URL;

public interface HttpResponse {

	URL getUrl();

	long getDuration();

	HttpHeader getHeader();

	HttpContent getContent();
}
