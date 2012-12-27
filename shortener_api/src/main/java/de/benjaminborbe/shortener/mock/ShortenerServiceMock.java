package de.benjaminborbe.shortener.mock;

import java.net.URL;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.shortener.api.ShortenerService;
import de.benjaminborbe.shortener.api.ShortenerServiceException;

@Singleton
public class ShortenerServiceMock implements ShortenerService {

	@Inject
	public ShortenerServiceMock() {
	}

	@Override
	public String shorten(final URL url) throws ShortenerServiceException {
		return null;
	}

	@Override
	public URL getUrl(final String token) throws ShortenerServiceException {
		return null;
	}

}
