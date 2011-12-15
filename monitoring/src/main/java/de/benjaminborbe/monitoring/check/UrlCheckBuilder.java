package de.benjaminborbe.monitoring.check;

import org.slf4j.Logger;

import com.google.inject.Inject;

import de.benjaminborbe.tools.util.HttpDownloader;

public class UrlCheckBuilder {

	private final Logger logger;

	private final HttpDownloader httpDownloader;

	@Inject
	public UrlCheckBuilder(final Logger logger, final HttpDownloader httpDownloader) {
		this.logger = logger;
		this.httpDownloader = httpDownloader;

	}

	public Check buildCheck(final String urlString, final String titleMatch, final String contentMatch) {
		return new UrlCheck(logger, httpDownloader, urlString, titleMatch, contentMatch);
	}

}
