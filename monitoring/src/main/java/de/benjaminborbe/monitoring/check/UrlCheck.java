package de.benjaminborbe.monitoring.check;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.slf4j.Logger;

import de.benjaminborbe.tools.util.HttpDownloadResult;
import de.benjaminborbe.tools.util.HttpDownloader;

public class UrlCheck implements Check {

	// 5 sec
	private static final int TIMEOUT = 5 * 1000;

	private final Logger logger;

	private final HttpDownloader httpDownloader;

	private final String urlString;

	public UrlCheck(final Logger logger, final HttpDownloader httpDownloader, final String urlString) {
		this.logger = logger;
		this.httpDownloader = httpDownloader;
		this.urlString = urlString;
	}

	@Override
	public boolean check() {
		try {
			final URL url = new URL(urlString);
			final HttpDownloadResult result = httpDownloader.downloadUrlUnsecure(url, TIMEOUT);
			logger.debug("MonitoringCronJob.execute() - downloaded " + url + " in " + result.getDuration() + " ms");
			return true;
		}
		catch (final MalformedURLException e) {
			logger.warn("MalformedURLException", e);
			return false;
		}
		catch (final IOException e) {
			logger.warn("IOException", e);
			return false;
		}
	}

}
