package de.benjaminborbe.gallery.service;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.gallery.api.GalleryService;

@Singleton
public class GalleryServiceImpl implements GalleryService {

	private final Logger logger;

	@Inject
	public GalleryServiceImpl(final Logger logger) {
		this.logger = logger;
	}

	@Override
	public void execute() {
		logger.trace("execute");
	}

}
