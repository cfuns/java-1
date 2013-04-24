package de.benjaminborbe.worktime.core.util;

import de.benjaminborbe.tools.http.HttpDownloadResult;
import de.benjaminborbe.tools.http.HttpDownloadUtil;
import de.benjaminborbe.tools.http.HttpDownloader;
import de.benjaminborbe.tools.http.HttpDownloaderException;
import de.benjaminborbe.tools.util.ParseException;
import de.benjaminborbe.tools.util.ParseUtil;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.io.IOException;
import java.net.URL;

public class InOfficeCheckHttpContent {

	// 5 seconds
	private static final int TIMEOUT = 5 * 1000;

	private static final String TIMETRACKER_URL = "https://timetracker.rp.seibert-media.net/timetracker/";

	private static final String TIMETRACKER_MATCH = "TimeTracker  | //SEIBERT/MEDIA TimeTracker / Login";

	private static final String PROJECTILE_URL = "https://projectile.rp.seibert-media.net/projectile/start";

	private static final String PROJECTILE_MATCH = "//SEIBERT/MEDIA GmbH";

	private final Logger logger;

	private final ParseUtil parseUtil;

	private final HttpDownloader httpDownloader;

	private final HttpDownloadUtil httpDownloadUtil;

	@Inject
	public InOfficeCheckHttpContent(final Logger logger, final ParseUtil parseUtil, final HttpDownloader httpDownloader, final HttpDownloadUtil httpDownloadUtil) {
		this.logger = logger;
		this.parseUtil = parseUtil;
		this.httpDownloader = httpDownloader;
		this.httpDownloadUtil = httpDownloadUtil;
	}

	public boolean check() {
		return check(PROJECTILE_URL, PROJECTILE_MATCH) && check(TIMETRACKER_URL, TIMETRACKER_MATCH);
	}

	private boolean check(final String urlString, final String matchString) {
		try {
			final URL url = parseUtil.parseURL(urlString);
			final HttpDownloadResult result = httpDownloader.getUrlUnsecure(url, TIMEOUT);
			logger.trace("downloaded " + url + " in " + result.getDuration() + " ms");
			if (result.getDuration() > TIMEOUT) {
				final String msg = "timeout while downloading url: " + url;
				logger.warn(msg);
				return false;
			}
			final String content = httpDownloadUtil.getContent(result);
			return content != null && content.contains(matchString);
		} catch (final ParseException | IOException e) {
			logger.warn(e.getClass().getName());
			return false;
		} catch (final HttpDownloaderException e) {
			logger.trace("HttpDownloaderException");
			return false;
		}
	}
}
