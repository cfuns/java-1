package de.benjaminborbe.shortener.api;

import java.net.URL;

import de.benjaminborbe.api.ValidationException;

public interface ShortenerService {

	ShortenerUrlIdentifier shorten(URL url) throws ShortenerServiceException, ValidationException;

	URL getUrl(ShortenerUrlIdentifier shortenerUrlIdentifier) throws ShortenerServiceException;
}
