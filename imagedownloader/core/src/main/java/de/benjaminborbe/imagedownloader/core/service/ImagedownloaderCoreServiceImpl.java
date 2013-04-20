package de.benjaminborbe.imagedownloader.core.service;

import javax.inject.Inject;
import javax.inject.Singleton;
import de.benjaminborbe.imagedownloader.api.ImagedownloaderService;
import de.benjaminborbe.imagedownloader.api.ImagedownloaderServiceException;
import org.slf4j.Logger;

import java.net.URL;

@Singleton
public class ImagedownloaderCoreServiceImpl implements ImagedownloaderService {

	private final Logger logger;

	@Inject
	public ImagedownloaderCoreServiceImpl(final Logger logger) {
		this.logger = logger;
	}

	@Override
	public void downloadImages(final URL url, final int depth) throws ImagedownloaderServiceException {
		logger.debug("downloadImages - url: " + url + " depth: " + depth);
	}

}
