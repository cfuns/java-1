package de.benjaminborbe.monitoring.check;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.slf4j.Logger;

import de.benjaminborbe.monitoring.api.Check;
import de.benjaminborbe.monitoring.api.CheckResult;
import de.benjaminborbe.tools.http.HttpDownloadResult;
import de.benjaminborbe.tools.http.HttpDownloadUtil;
import de.benjaminborbe.tools.http.HttpDownloader;
import de.benjaminborbe.tools.http.HttpDownloaderException;

public class UrlCheck implements Check {

	// 5 sec
	private static final int TIMEOUT = 5 * 1000;

	private final Logger logger;

	private final HttpDownloader httpDownloader;

	private final String urlString;

	private final String titleMatch;

	private final String contentMatch;

	private final String name;

	private final String username;

	private final String password;

	private final HttpDownloadUtil httpDownloadUtil;

	public UrlCheck(
			final Logger logger,
			final HttpDownloader httpDownloader,
			final HttpDownloadUtil httpDownloadUtil,
			final String name,
			final String urlString,
			final String titleMatch,
			final String contentMatch) {
		this(logger, httpDownloader, httpDownloadUtil, name, urlString, titleMatch, contentMatch, null, null);
	}

	public UrlCheck(
			final Logger logger,
			final HttpDownloader httpDownloader,
			final HttpDownloadUtil httpDownloadUtil,
			final String name,
			final String urlString,
			final String titleMatch,
			final String contentMatch,
			final String username,
			final String password) {
		this.httpDownloadUtil = httpDownloadUtil;
		this.logger = logger;
		this.httpDownloader = httpDownloader;
		this.name = name;
		this.urlString = urlString;
		this.titleMatch = titleMatch;
		this.contentMatch = contentMatch;
		this.username = username;
		this.password = password;
	}

	@Override
	public CheckResult check() {
		try {
			final URL url = new URL(urlString);
			final HttpDownloadResult result;
			if (username != null && password != null) {
				result = httpDownloader.downloadUrlUnsecure(url, TIMEOUT, username, password);
			}
			else {
				result = httpDownloader.downloadUrlUnsecure(url, TIMEOUT);
			}
			logger.debug("downloaded " + url + " in " + result.getDuration() + " ms");
			if (result.getDuration() > TIMEOUT) {
				final String msg = "timeout while downloading url: " + url;
				logger.warn(msg);
				return new CheckResultImpl(this, false, msg);
			}
			final String content = httpDownloadUtil.getContent(result);
			if (!checkTitle(content)) {
				final String msg = "cannot find title " + titleMatch + " in content of " + url;
				logger.warn(msg);
				return new CheckResultImpl(this, false, msg);
			}
			if (!checkContent(content)) {
				final String msg = "cannot find string " + contentMatch + " in content of " + url;
				logger.warn(msg);
				return new CheckResultImpl(this, false, msg);
			}
			final String msg = "download url successful " + url;
			return new CheckResultImpl(this, true, msg);
		}
		catch (final MalformedURLException e) {
			logger.warn("MalformedURLException", e);
			return new CheckResultImpl(this, false, "MalformedURLException");
		}
		catch (final IOException e) {
			logger.warn("IOException", e);
			return new CheckResultImpl(this, false, "IOException");
		}
		catch (final HttpDownloaderException e) {
			logger.warn("HttpDownloaderException", e);
			return new CheckResultImpl(this, false, "HttpDownloaderException");
		}
	}

	protected boolean checkContent(final String content) {
		return contentMatch == null || content.indexOf(contentMatch) != -1;
	}

	protected boolean checkTitle(final String content) {
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

	@Override
	public String getDescription() {
		return "check url: " + urlString + " titleMatch: " + titleMatch + " contentMatch: " + contentMatch;
	}

	@Override
	public String getName() {
		return name;
	}

	public String getTitleMatch() {
		return titleMatch;
	}

	public String getContentMatch() {
		return contentMatch;
	}

}
