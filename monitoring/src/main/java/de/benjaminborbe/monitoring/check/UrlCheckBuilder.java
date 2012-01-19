package de.benjaminborbe.monitoring.check;

import org.slf4j.Logger;

import com.google.inject.Inject;

import de.benjaminborbe.tools.http.HttpDownloadUtil;
import de.benjaminborbe.tools.http.HttpDownloader;

public class UrlCheckBuilder {

	private final Logger logger;

	private final HttpDownloader httpDownloader;

	private final HttpDownloadUtil httpDownloadUtil;

	@Inject
	public UrlCheckBuilder(final Logger logger, final HttpDownloader httpDownloader, final HttpDownloadUtil httpDownloadUtil) {
		this.logger = logger;
		this.httpDownloader = httpDownloader;
		this.httpDownloadUtil = httpDownloadUtil;

	}

	public Check buildCheck(final String name, final String urlString, final String titleMatch, final String contentMatch) {
		return buildCheck(name, urlString, titleMatch, contentMatch, null, null);
	}

	public Check buildCheck(final String name, final String urlString, final String titleMatch, final String contentMatch, final String username, final String password) {
		return new UrlCheck(logger, httpDownloader, httpDownloadUtil, name, urlString, titleMatch, contentMatch, username, password);
	}
}
