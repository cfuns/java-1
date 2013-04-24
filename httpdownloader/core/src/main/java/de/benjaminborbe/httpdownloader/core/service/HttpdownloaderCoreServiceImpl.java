package de.benjaminborbe.httpdownloader.core.service;

import de.benjaminborbe.httpdownloader.api.HttpdownloaderService;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class HttpdownloaderCoreServiceImpl implements HttpdownloaderService {

	private final Logger logger;

	@Inject
	public HttpdownloaderCoreServiceImpl(final Logger logger) {
		this.logger = logger;
	}

	@Override
	public long calc(final long value) {
		logger.trace("execute");
		return value * 2;
	}

}
