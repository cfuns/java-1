package de.benjaminborbe.shortener.api;

import de.benjaminborbe.api.ValidationException;

import java.net.URL;

public interface ShortenerService {

	ShortenerUrlIdentifier shorten(URL url) throws ShortenerServiceException, ValidationException;

	URL getUrl(ShortenerUrlIdentifier shortenerUrlIdentifier) throws ShortenerServiceException;
}
