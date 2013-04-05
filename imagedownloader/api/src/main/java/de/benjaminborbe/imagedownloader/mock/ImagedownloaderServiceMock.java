package de.benjaminborbe.imagedownloader.mock;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import de.benjaminborbe.imagedownloader.api.ImagedownloaderService;

@Singleton
public class ImagedownloaderServiceMock implements ImagedownloaderService {

	@Inject
	public ImagedownloaderServiceMock() {
	}

	@Override
	public long calc(final long value) {
		return 0;
	}
}
