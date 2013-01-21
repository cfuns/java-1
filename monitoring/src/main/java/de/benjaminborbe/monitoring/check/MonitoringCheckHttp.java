package de.benjaminborbe.monitoring.check;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import org.slf4j.Logger;

import com.google.inject.Inject;

import de.benjaminborbe.monitoring.api.MonitoringCheckType;
import de.benjaminborbe.tools.http.HttpDownloadUtil;
import de.benjaminborbe.tools.http.HttpDownloader;
import de.benjaminborbe.tools.http.HttpDownloaderException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import de.benjaminborbe.tools.http.HttpDownloadResult;
import de.benjaminborbe.tools.util.ParseException;
import de.benjaminborbe.tools.util.ParseUtil;

public class MonitoringCheckHttp implements MonitoringCheck {

	private static final String URL = "url";

	private static final String TIMEOUT = "timeout";

	private static final String TITLEMATCH = "titlematch";

	private static final String CONTENTMATCH = "contentmatch";

	private static final String USERNAME = "username";

	private static final String PASSWORD = "password";

	private final Logger logger;

	private final HttpDownloader httpDownloader;

	private final HttpDownloadUtil httpDownloadUtil;

	private final ParseUtil parseUtil;

	@Inject
	public MonitoringCheckHttp(final Logger logger, final HttpDownloader httpDownloader, final HttpDownloadUtil httpDownloadUtil, final ParseUtil parseUtil) {
		this.logger = logger;
		this.httpDownloader = httpDownloader;
		this.httpDownloadUtil = httpDownloadUtil;
		this.parseUtil = parseUtil;
	}

	@Override
	public MonitoringCheckType getType() {
		return MonitoringCheckType.HTTP;
	}

	@Override
	public Collection<String> getRequireParameters() {
		return Arrays.asList(URL, TIMEOUT, TITLEMATCH, CONTENTMATCH, USERNAME, PASSWORD);
	}

	@Override
	public MonitoringCheckResult check(final Map<String, String> parameter) {
		final String urlString = parameter.get(URL);
		final String titleMatch = parameter.get(TITLEMATCH);
		final String contentMatch = parameter.get(CONTENTMATCH);
		final String username = parameter.get(USERNAME);
		final String password = parameter.get(PASSWORD);
		final int timeout;
		try {
			timeout = parseUtil.parseInt(parameter.get(TIMEOUT));
		}
		catch (final ParseException e) {
			return new MonitoringCheckResultDto(this, false, "illegal paremter " + TIMEOUT);
		}
		return check(urlString, timeout, titleMatch, contentMatch, username, password);
	}

	private MonitoringCheckResult check(final String urlString, final int timeout, final String titleMatch, final String contentMatch, final String username, final String password) {
		URL url = null;
		try {
			url = new URL(urlString);
			final HttpDownloadResult result;
			if (username != null && password != null) {
				result = httpDownloader.getUrlUnsecure(url, timeout, username, password);
			}
			else {
				result = httpDownloader.getUrlUnsecure(url, timeout);
			}
			logger.trace("downloaded " + url + " in " + result.getDuration() + " ms");
			if (result.getDuration() > timeout) {
				final String msg = "timeout while downloading url: " + url;
				logger.trace(msg);
				return new MonitoringCheckResultDto(this, false, msg, url);
			}
			final String content = httpDownloadUtil.getContent(result);
			if (!checkTitle(content, titleMatch)) {
				final String msg = "cannot find title " + titleMatch + " in content of " + url;
				logger.warn(msg);
				return new MonitoringCheckResultDto(this, false, msg, url);
			}
			if (!checkContent(content, contentMatch)) {
				final String msg = "cannot find string " + contentMatch + " in content of " + url;
				logger.warn(msg);
				return new MonitoringCheckResultDto(this, false, msg, url);
			}
			final String msg = "download url successful " + url;
			return new MonitoringCheckResultDto(this, true, msg, url);
		}
		catch (final MalformedURLException e) {
			logger.warn("MalformedURLException", e);
			return new MonitoringCheckResultDto(this, e, url);
		}
		catch (final IOException e) {
			logger.warn("IOException", e);
			return new MonitoringCheckResultDto(this, e, url);
		}
		catch (final HttpDownloaderException e) {
			logger.trace("HttpDownloaderException: " + e.getMessage());
			return new MonitoringCheckResultDto(this, e, url);
		}
	}

	private boolean checkContent(final String content, final String contentMatch) {
		return contentMatch == null || content.indexOf(contentMatch) != -1;
	}

	private boolean checkTitle(final String content, final String titleMatch) {
		if (titleMatch == null)
			return true;
		if (content == null)
			return false;
		final int posStart = content.indexOf("<title>");
		final int posEnd = content.indexOf("</title>");
		if (posStart == -1 || posEnd == -1)
			return false;
		final String title = content.substring(posStart, posEnd);
		return title.indexOf(titleMatch) != -1;
	}

}
