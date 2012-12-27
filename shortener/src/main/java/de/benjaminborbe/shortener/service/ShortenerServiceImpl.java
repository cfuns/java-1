package de.benjaminborbe.shortener.service;

import java.net.URL;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.shortener.api.ShortenerService;
import de.benjaminborbe.shortener.api.ShortenerServiceException;

@Singleton
public class ShortenerServiceImpl implements ShortenerService {

	private final Logger logger;

	@Inject
	public ShortenerServiceImpl(final Logger logger) {
		this.logger = logger;
	}

	@Override
	public String shorten(final URL url) throws ShortenerServiceException {
		logger.trace("shorten");
		return null;
	}

	@Override
	public URL getUrl(final String token) throws ShortenerServiceException {
		logger.trace("getUrl");
		return null;
	}

}
