package de.benjaminborbe.imagedownloader.core.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import de.benjaminborbe.imagedownloader.api.ImagedownloaderService;
import org.slf4j.Logger;

@Singleton
public class ImagedownloaderCoreServiceImpl implements ImagedownloaderService {

	private final Logger logger;

	@Inject
	public ImagedownloaderCoreServiceImpl(final Logger logger) {
		this.logger = logger;
	}

	@Override
	public long calc(final long value) {
		logger.trace("execute");
		return value * 2;
	}

}
