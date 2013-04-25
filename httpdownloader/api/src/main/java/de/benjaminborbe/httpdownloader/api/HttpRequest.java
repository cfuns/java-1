package de.benjaminborbe.httpdownloader.api;

import java.net.URL;

public interface HttpRequest {

	HttpHeader getHeader();

	URL getUrl();

	int getTimeout();

}
