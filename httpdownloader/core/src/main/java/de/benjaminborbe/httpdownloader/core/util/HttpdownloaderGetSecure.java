package de.benjaminborbe.httpdownloader.core.util;

import de.benjaminborbe.tools.http.HttpDownloadResult;
import de.benjaminborbe.tools.http.HttpDownloader;
import de.benjaminborbe.tools.http.HttpDownloaderException;

import javax.inject.Inject;
import java.net.URL;

public class HttpdownloaderGetSecure implements HttpdownloaderGet {

	private final HttpDownloader httpDownloader;

	@Inject
	public HttpdownloaderGetSecure(final HttpDownloader httpDownloader) {
		this.httpDownloader = httpDownloader;
	}

	public HttpDownloadResult getUrl(final URL url, final int timeOut) throws HttpDownloaderException {
		return httpDownloader.getUrl(url, timeOut);
	}
}
