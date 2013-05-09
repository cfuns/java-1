package de.benjaminborbe.websearch.core.util;

import de.benjaminborbe.httpdownloader.api.HttpResponse;
import de.benjaminborbe.httpdownloader.api.HttpdownloaderService;
import de.benjaminborbe.httpdownloader.api.HttpdownloaderServiceException;
import de.benjaminborbe.httpdownloader.tools.HttpRequestDto;
import de.benjaminborbe.httpdownloader.tools.HttpUtil;
import de.benjaminborbe.tools.http.HttpDownloader;
import de.benjaminborbe.tools.util.ParseException;
import de.benjaminborbe.tools.util.ParseUtil;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class WebsearchRobotsTxtUtil {

	private static final int TIMEOUT = 5000;

	private final ParseUtil parseUtil;

	private final WebsearchRobotsTxtParser websearchRobotsTxtParser;

	private final HttpdownloaderService httpdownloaderService;

	private final HttpUtil httpUtil;

	private final Logger logger;

	private final Map<String, WebsearchRobotsTxt> cache = new HashMap<>();

	@Inject
	public WebsearchRobotsTxtUtil(
		final Logger logger,
		final ParseUtil parseUtil,
		final WebsearchRobotsTxtParser websearchRobotsTxtParser,
		final HttpdownloaderService httpdownloaderService,
		final HttpUtil httpUtil
	) {
		this.logger = logger;
		this.parseUtil = parseUtil;
		this.websearchRobotsTxtParser = websearchRobotsTxtParser;
		this.httpdownloaderService = httpdownloaderService;
		this.httpUtil = httpUtil;
	}

	public boolean isAllowed(final URL url) {
		logger.trace("check url: " + url);
		final WebsearchRobotsTxt robotsTxt = getWebsearchRobotsTxt(url.toExternalForm());
		final String uri = buildUri(url.toExternalForm());
		final boolean allowed = robotsTxt.isAllowed(HttpDownloader.USERAGENT, uri);
		logger.debug("url: " + url + " is " + (allowed ? "allowed" : "disallowed") + " for ua: " + HttpDownloader.USERAGENT + " via robots.txt");
		return allowed;
	}

	private WebsearchRobotsTxt getWebsearchRobotsTxt(final String url) {
		final String robotsTxtUrl = buildRobotsTxtUrl(url);
		if (cache.containsKey(robotsTxtUrl)) {
			return cache.get(robotsTxtUrl);
		} else {
			try {
				final HttpResponse httpResponse = httpdownloaderService.getUnsecure(new HttpRequestDto(parseUtil.parseURL(url), TIMEOUT));
				final String content = httpUtil.getContent(httpResponse);
				final WebsearchRobotsTxt robotstxt = websearchRobotsTxtParser.parseRobotsTxt(content);
				cache.put(robotsTxtUrl, robotstxt);
				return robotstxt;
			} catch (final ParseException | IOException | HttpdownloaderServiceException e) {
				logger.trace("download robots.txt failed!", e);
			}
			final WebsearchRobotsTxt robotstxt = new WebsearchRobotsTxt();
			cache.put(robotsTxtUrl, robotstxt);
			return robotstxt;
		}
	}

	protected String buildUri(final String url) {
		final int pos1 = url.indexOf("://");
		final int pos2 = url.indexOf("/", pos1 + 4);
		if (pos2 == -1) {
			return "/";
		} else {
			return url.substring(pos2);
		}
	}

	protected String buildRobotsTxtUrl(final String url) {
		final int pos1 = url.indexOf("://");
		final int pos2 = url.indexOf("/", pos1 + 4);
		if (pos2 == -1) {
			return url + "/robots.txt";
		} else {
			return url.substring(0, pos2) + "/robots.txt";
		}
	}
}
