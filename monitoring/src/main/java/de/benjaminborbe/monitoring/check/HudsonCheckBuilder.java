package de.benjaminborbe.monitoring.check;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.tools.http.HttpDownloader;

@Singleton
public class HudsonCheckBuilder {

	private final Logger logger;

	private final HttpDownloader httpDownloader;

	@Inject
	public HudsonCheckBuilder(final Logger logger, final HttpDownloader httpDownloader) {
		this.logger = logger;
		this.httpDownloader = httpDownloader;
	}

	public Check buildCheck(final String name, final String hostname, final String job) {
		return new HudsonCheck(logger, httpDownloader, name, hostname, job);
	}
}
