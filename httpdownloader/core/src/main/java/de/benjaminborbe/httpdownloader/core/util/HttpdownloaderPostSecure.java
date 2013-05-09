package de.benjaminborbe.httpdownloader.core.util;

import de.benjaminborbe.httpdownloader.api.HttpRequest;

import javax.inject.Inject;

public class HttpdownloaderPostSecure implements HttpdownloaderPost {

	private final HttpDownloader httpDownloader;

	@Inject
	public HttpdownloaderPostSecure(final HttpDownloader httpDownloader) {
		this.httpDownloader = httpDownloader;
	}

	@Override
	public HttpDownloadResult fetch(final HttpRequest httpRequest) throws HttpDownloaderException {
		return httpDownloader.postUrlSecure(httpRequest.getUrl(), httpRequest.getTimeout(), httpRequest.getParameter(), httpRequest.getHeader());
	}
}
