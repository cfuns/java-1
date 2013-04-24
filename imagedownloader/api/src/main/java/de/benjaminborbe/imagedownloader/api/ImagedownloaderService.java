package de.benjaminborbe.imagedownloader.api;

import java.net.URL;

public interface ImagedownloaderService {

	void downloadImages(URL url, int depth, final int minWidth, final int minHeight) throws ImagedownloaderServiceException;

}
