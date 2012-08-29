package de.benjaminborbe.worktime.util;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.slf4j.Logger;

import com.google.inject.Inject;

import de.benjaminborbe.tools.http.HttpDownloadResult;
import de.benjaminborbe.tools.http.HttpDownloadUtil;
import de.benjaminborbe.tools.http.HttpDownloader;
import de.benjaminborbe.tools.http.HttpDownloaderException;

public class InOfficeCheckHttpContent {

	// 5 seconds
	private static final int TIMEOUT = 5 * 1000;

	private static final String TIMETRACKER_URL = "https://timetracker.rp.seibert-media.net/timetracker/";

	private static final String TIMETRACKER_MATCH = "TimeTracker  | //SEIBERT/MEDIA TimeTracker / Login";

	private static final String PROJECTILE_URL = "https://projectile.rp.seibert-media.net/projectile/start";

	private static final String PROJECTILE_MATCH = "//SEIBERT/MEDIA GmbH";

	private final Logger logger;

	private final HttpDownloader httpDownloader;

	private final HttpDownloadUtil httpDownloadUtil;

	@Inject
	public InOfficeCheckHttpContent(final Logger logger, final HttpDownloader httpDownloader, final HttpDownloadUtil httpDownloadUtil) {
		this.logger = logger;
		this.httpDownloader = httpDownloader;
		this.httpDownloadUtil = httpDownloadUtil;
	}

	public boolean check() {
		return check(PROJECTILE_URL, PROJECTILE_MATCH) && check(TIMETRACKER_URL, TIMETRACKER_MATCH);
	}

	private boolean check(final String urlString, final String matchString) {
		try {
			final URL url = new URL(urlString);
			final HttpDownloadResult result = httpDownloader.getUrlUnsecure(url, TIMEOUT);
			logger.trace("downloaded " + url + " in " + result.getDuration() + " ms");
			if (result.getDuration() > TIMEOUT) {
				final String msg = "timeout while downloading url: " + url;
				logger.warn(msg);
				return false;
			}
			final String content = httpDownloadUtil.getContent(result);
			return content != null && content.indexOf(matchString) != -1;
		}
		catch (final MalformedURLException e) {
			logger.warn("MalformedURLException");
			return false;
		}
		catch (final IOException e) {
			logger.warn("IOException");
			return false;
		}
		catch (final HttpDownloaderException e) {
			logger.warn("HttpDownloaderException");
			return false;
		}
	}
}
