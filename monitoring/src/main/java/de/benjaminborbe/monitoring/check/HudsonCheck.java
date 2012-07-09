package de.benjaminborbe.monitoring.check;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;

import org.slf4j.Logger;

import de.benjaminborbe.monitoring.api.Check;
import de.benjaminborbe.monitoring.api.CheckResult;
import de.benjaminborbe.tools.http.HttpDownloadResult;
import de.benjaminborbe.tools.http.HttpDownloadUtil;
import de.benjaminborbe.tools.http.HttpDownloader;
import de.benjaminborbe.tools.http.HttpDownloaderException;

public class HudsonCheck implements Check {

	// 3 sec
	private static final int TIMEOUT = 3 * 1000;

	private final String hudsonUrl;

	private final String job;

	private final String name;

	private final HttpDownloader httpDownloader;

	private final Logger logger;

	private final HttpDownloadUtil httpDownloadUtil;

	private final String username;

	private final String password;

	public HudsonCheck(
			final Logger logger,
			final HttpDownloader httpDownloader,
			final HttpDownloadUtil httpDownloadUtil,
			final String name,
			final String hudsonUrl,
			final String job,
			final String username,
			final String password) {
		this.logger = logger;
		this.httpDownloader = httpDownloader;
		this.httpDownloadUtil = httpDownloadUtil;
		this.name = name;
		this.hudsonUrl = hudsonUrl;
		this.job = job;
		this.username = username;
		this.password = password;
	}

	public HudsonCheck(final Logger logger, final HttpDownloader httpDownloader, final HttpDownloadUtil httpDownloadUtil, final String name, final String hudsonUrl, final String job) {
		this(logger, httpDownloader, httpDownloadUtil, name, hudsonUrl, job, null, null);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getDescription() {
		return "Hudson check on host " + hudsonUrl + " for job " + job;
	}

	@Override
	public CheckResult check() {
		URL url = null;
		try {
			url = new URL(hudsonUrl);
			final HttpDownloadResult result = httpDownloader.downloadUrlUnsecure(url, TIMEOUT, username, password);
			final String content = httpDownloadUtil.getContent(result);
			final String row = getRow(content);
			// hudson not valid
			if (row == null) {
				final String msg = "Parse HTMLContent failed";
				logger.trace(msg);
				return new CheckResultImpl(this, false, msg, url);
			}
			// found unstable => false
			else if (row.indexOf(" alt=\"Unstable\" ") != -1) {
				final String msg = "is unstable";
				logger.trace(msg);
				return new CheckResultImpl(this, false, msg, url);
			}
			// found failed => false
			else if (row.indexOf(" alt=\"Failed\" ") != -1) {
				final String msg = "is failed";
				logger.trace(msg);
				return new CheckResultImpl(this, false, msg, url);
			}
			// found failed => false
			else if (row.indexOf(" alt=\"In progress\" ") != -1) {
				final String msg = "is in progress";
				logger.trace(msg);
				return new CheckResultImpl(this, true, msg, url);
			}
			// found no unstable => true
			else {
				final String msg = "is stable";
				logger.trace(msg);
				return new CheckResultImpl(this, true, msg, url);
			}
		}
		catch (final MalformedURLException e) {
			logger.warn("MalformedURLException", e);
			return new CheckResultException(this, e, url);
		}
		catch (final HttpDownloaderException e) {
			logger.warn("download " + url + " failed");
			return new CheckResultException(this, e, url);
		}
		catch (final UnsupportedEncodingException e) {
			logger.warn("UnsupportedEncodingException", e);
			return new CheckResultException(this, e, url);
		}
	}

	private String getRow(final String content) {
		logger.trace("content '" + content + "'");

		final String posString = ">" + job + "</a></td><td data=\"";
		final int pos = content.indexOf(posString);
		if (pos == -1) {
			logger.warn("posString '" + posString + "' not found in content");
			return null;
		}

		final String startString = job + "\"><td data=\"";
		final int start = content.lastIndexOf(startString, pos);
		if (start == -1) {
			logger.warn("startString '" + startString + "' not found in content");
			return null;
		}

		final String endString = "</tr>";
		final int end = content.indexOf(endString, pos);
		if (end == -1) {
			logger.warn("endString '" + endString + "' not found in content");
			return null;
		}

		final String result = content.substring(start + startString.length(), end);
		logger.debug("found row content '" + result + "'");
		return result;
	}

}
