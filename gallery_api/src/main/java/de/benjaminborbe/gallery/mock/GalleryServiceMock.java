package de.benjaminborbe.gallery.mock;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.gallery.api.GalleryService;

@Singleton
public class GalleryServiceMock implements GalleryService {

	@Inject
	public GalleryServiceMock() {
	}

	@Override
	public void execute() {
	}
}
