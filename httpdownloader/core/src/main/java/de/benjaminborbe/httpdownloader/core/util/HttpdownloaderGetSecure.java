package de.benjaminborbe.httpdownloader.core.util;

import de.benjaminborbe.httpdownloader.api.HttpRequest;

import javax.inject.Inject;

public class HttpdownloaderGetSecure implements HttpdownloaderGet {

	private final HttpDownloader httpDownloader;

	@Inject
	public HttpdownloaderGetSecure(final HttpDownloader httpDownloader) {
		this.httpDownloader = httpDownloader;
	}

	@Override
	public HttpDownloadResult fetch(final HttpRequest httpRequest) throws HttpDownloaderException {
		return httpDownloader.getUrlSecure(httpRequest.getUrl(), httpRequest.getTimeout(), httpRequest.getUsername(), httpRequest.getPassword(), httpRequest.getHeader());
	}
}
