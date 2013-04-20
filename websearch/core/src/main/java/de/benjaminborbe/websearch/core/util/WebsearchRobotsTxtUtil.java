package de.benjaminborbe.websearch.core.util;

import javax.inject.Inject;
import de.benjaminborbe.tools.http.HttpDownloadResult;
import de.benjaminborbe.tools.http.HttpDownloadUtil;
import de.benjaminborbe.tools.http.HttpDownloader;
import de.benjaminborbe.tools.http.HttpDownloaderException;
import org.slf4j.Logger;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class WebsearchRobotsTxtUtil {

	private static final int TIMEOUT = 5000;

	private final WebsearchRobotsTxtParser websearchRobotsTxtParser;

	private final HttpDownloader httpDownloader;

	private final HttpDownloadUtil httpDownloadUtil;

	private final Logger logger;

	private final Map<String, WebsearchRobotsTxt> cache = new HashMap<>();

	@Inject
	public WebsearchRobotsTxtUtil(
		final Logger logger,
		final WebsearchRobotsTxtParser websearchRobotsTxtParser,
		final HttpDownloader httpDownloader,
		final HttpDownloadUtil httpDownloadUtil) {
		this.logger = logger;
		this.websearchRobotsTxtParser = websearchRobotsTxtParser;
		this.httpDownloader = httpDownloader;
		this.httpDownloadUtil = httpDownloadUtil;
	}

	public boolean isAllowed(final String url) {
		logger.debug("isAllowed - url: " + url);
		final WebsearchRobotsTxt robotsTxt = getWebsearchRobotsTxt(url);
		final String uri = buildUri(url);
		logger.debug("isAllowed - ua: " + HttpDownloader.USERAGENT + " uri: " + uri);
		return robotsTxt.isAllowed(HttpDownloader.USERAGENT, uri);
	}

	private WebsearchRobotsTxt getWebsearchRobotsTxt(final String url) {
		final String robotsTxtUrl = buildRobotsTxtUrl(url);
		if (cache.containsKey(robotsTxtUrl)) {
			return cache.get(robotsTxtUrl);
		} else {
			try {
				final HttpDownloadResult result = httpDownloader.getUrlUnsecure(new URL(robotsTxtUrl), TIMEOUT);
				final String content = httpDownloadUtil.getContent(result);
				final WebsearchRobotsTxt robotstxt = websearchRobotsTxtParser.parseRobotsTxt(content);
				cache.put(robotsTxtUrl, robotstxt);
				return robotstxt;
			} catch (final MalformedURLException | UnsupportedEncodingException | HttpDownloaderException e) {
				// nop
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
