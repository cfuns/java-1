package de.benjaminborbe.imagedownloader.api;

import java.net.URL;

public interface ImagedownloaderService {

	void downloadImages(URL url, int depth) throws ImagedownloaderServiceException;

}
