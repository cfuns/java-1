package de.benjaminborbe.shortener.mock;

import java.net.URL;


import de.benjaminborbe.shortener.api.ShortenerService;
import de.benjaminborbe.shortener.api.ShortenerServiceException;
import de.benjaminborbe.shortener.api.ShortenerUrlIdentifier;

public class ShortenerServiceMock implements ShortenerService {

	public ShortenerServiceMock() {
	}

	@Override
	public ShortenerUrlIdentifier shorten(final URL url) throws ShortenerServiceException {
		return null;
	}

	@Override
	public URL getUrl(final ShortenerUrlIdentifier token) throws ShortenerServiceException {
		return null;
	}

}
