package de.benjaminborbe.imagedownloader.mock;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import de.benjaminborbe.imagedownloader.api.ImagedownloaderService;
import de.benjaminborbe.imagedownloader.api.ImagedownloaderServiceException;

import java.net.URL;

@Singleton
public class ImagedownloaderServiceMock implements ImagedownloaderService {

	@Inject
	public ImagedownloaderServiceMock() {
	}

	@Override
	public void downloadImages(final URL url, final int depth) throws ImagedownloaderServiceException {
	}
}
