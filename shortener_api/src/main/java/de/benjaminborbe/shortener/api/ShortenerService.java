package de.benjaminborbe.shortener.api;

import java.net.URL;

public interface ShortenerService {

	String shorten(URL url) throws ShortenerServiceException;

	URL getUrl(String token) throws ShortenerServiceException;
}
