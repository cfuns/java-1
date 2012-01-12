package de.benjaminborbe.monitoring.check;

import org.slf4j.Logger;

import com.google.inject.Inject;

import de.benjaminborbe.tools.http.HttpDownloader;

public class UrlCheckBuilder {

	private final Logger logger;

	private final HttpDownloader httpDownloader;

	@Inject
	public UrlCheckBuilder(final Logger logger, final HttpDownloader httpDownloader) {
		this.logger = logger;
		this.httpDownloader = httpDownloader;

	}

	public Check buildCheck(final String name, final String urlString, final String titleMatch, final String contentMatch) {
		return buildCheck(name, urlString, titleMatch, contentMatch, null, null);
	}

	public Check buildCheck(final String name, final String urlString, final String titleMatch, final String contentMatch, final String username, final String password) {
		return new UrlCheck(logger, httpDownloader, name, urlString, titleMatch, contentMatch, username, password);
	}
}
