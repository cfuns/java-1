package de.benjaminborbe.imagedownloader.mock;

import de.benjaminborbe.imagedownloader.api.ImagedownloaderService;
import de.benjaminborbe.imagedownloader.api.ImagedownloaderServiceException;

import java.net.URL;

public class ImagedownloaderServiceMock implements ImagedownloaderService {

	public ImagedownloaderServiceMock() {
	}

	@Override
	public void downloadImages(final URL url, final int depth) throws ImagedownloaderServiceException {
	}
}
