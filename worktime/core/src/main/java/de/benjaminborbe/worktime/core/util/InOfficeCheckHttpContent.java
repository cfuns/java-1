package de.benjaminborbe.worktime.core.util;

import de.benjaminborbe.httpdownloader.api.HttpResponse;
import de.benjaminborbe.httpdownloader.api.HttpdownloaderService;
import de.benjaminborbe.httpdownloader.api.HttpdownloaderServiceException;
import de.benjaminborbe.httpdownloader.tools.HttpRequestBuilder;
import de.benjaminborbe.httpdownloader.tools.HttpUtil;
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

	private final HttpdownloaderService httpdownloaderService;

	private final HttpUtil httpUtil;

	@Inject
	public InOfficeCheckHttpContent(
		final Logger logger,
		final ParseUtil parseUtil,
		final HttpdownloaderService httpdownloaderService,
		final HttpUtil httpUtil
	) {
		this.logger = logger;
		this.parseUtil = parseUtil;
		this.httpdownloaderService = httpdownloaderService;
		this.httpUtil = httpUtil;
	}

	public boolean check() {
		return check(PROJECTILE_URL, PROJECTILE_MATCH) && check(TIMETRACKER_URL, TIMETRACKER_MATCH);
	}

	private boolean check(final String urlString, final String matchString) {
		try {
			final URL url = parseUtil.parseURL(urlString);
			final HttpResponse httpResponse = httpdownloaderService.fetch(new HttpRequestBuilder(url).addTimeout(TIMEOUT).build());
			logger.trace("downloaded " + url + " in " + httpResponse.getDuration() + " ms");
			if (httpResponse.getDuration() > TIMEOUT) {
				final String msg = "timeout while downloading url: " + url;
				logger.warn(msg);
				return false;
			}
			final String content = httpUtil.getContent(httpResponse);
			return content != null && content.contains(matchString);
		} catch (final HttpdownloaderServiceException e) {
			logger.warn(e.getClass().getName());
			return false;
		} catch (ParseException e) {
			logger.warn(e.getClass().getName());
			return false;
		} catch (IOException e) {
			logger.warn(e.getClass().getName());
			return false;
		}
	}
}
