package de.benjaminborbe.monitoring.check;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.monitoring.api.MonitoringCheck;
import de.benjaminborbe.tools.http.HttpDownloadUtil;
import de.benjaminborbe.tools.http.HttpDownloader;

@Singleton
public class HudsonCheckBuilder {

	private final Logger logger;

	private final HttpDownloader httpDownloader;

	private final HttpDownloadUtil httpDownloadUtil;

	@Inject
	public HudsonCheckBuilder(final Logger logger, final HttpDownloader httpDownloader, final HttpDownloadUtil httpDownloadUtil) {
		this.logger = logger;
		this.httpDownloader = httpDownloader;
		this.httpDownloadUtil = httpDownloadUtil;
	}

	public MonitoringCheck buildCheck(final String name, final String hostname, final String job) {
		return new HudsonCheck(logger, httpDownloader, httpDownloadUtil, name, hostname, job);
	}

	public MonitoringCheck buildCheck(final String name, final String hostname, final String job, final String username, final String password) {
		return new HudsonCheck(logger, httpDownloader, httpDownloadUtil, name, hostname, job, username, password);
	}
}
