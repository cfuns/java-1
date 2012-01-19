package de.benjaminborbe.monitoring.check;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.slf4j.Logger;

import de.benjaminborbe.tools.http.HttpDownloadResult;
import de.benjaminborbe.tools.http.HttpDownloadUtil;
import de.benjaminborbe.tools.http.HttpDownloader;

public class HudsonCheck implements Check {

	// 3 sec
	private static final int TIMEOUT = 3 * 1000;

	private final String hudsonUrl;

	private final String job;

	private final String name;

	private final HttpDownloader httpDownloader;

	private final Logger logger;

	private final HttpDownloadUtil httpDownloadUtil;

	public HudsonCheck(final Logger logger, final HttpDownloader httpDownloader, final HttpDownloadUtil httpDownloadUtil, final String name, final String hudsonUrl, final String job) {
		this.logger = logger;
		this.httpDownloader = httpDownloader;
		this.httpDownloadUtil = httpDownloadUtil;
		this.name = name;
		this.hudsonUrl = hudsonUrl;
		this.job = job;
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
		try {
			final URL url = new URL(hudsonUrl);
			final HttpDownloadResult result = httpDownloader.downloadUrlUnsecure(url, TIMEOUT);
			final String content = httpDownloadUtil.getContent(result);
			final String row = getRow(content);
			// hudson not valid
			if (row == null) {
				final String msg = "Parse Hudson-Content failed";
				logger.debug(msg);
				return new CheckResultImpl(this, false, msg);
			}
			// found unstable => false
			else if (row.indexOf(" alt=\"Unstable\" ") != -1) {
				final String msg = "Job: " + job + " on Hudson: " + hudsonUrl + " is unstable";
				logger.debug(msg);
				return new CheckResultImpl(this, false, msg);
			}
			// found failed => false
			else if (row.indexOf(" alt=\"Failed\" ") != -1) {
				final String msg = "Job: " + job + " on Hudson: " + hudsonUrl + " is failed";
				logger.debug(msg);
				return new CheckResultImpl(this, false, msg);
			}
			// found failed => false
			else if (row.indexOf(" alt=\"In progress\" ") != -1) {
				final String msg = "Job: " + job + " on Hudson: " + hudsonUrl + " is in progress";
				logger.debug(msg);
				return new CheckResultImpl(this, true, msg);
			}
			// found no unstable => true
			else {
				final String msg = "Job: " + job + " on Hudson: " + hudsonUrl + " is stable";
				logger.debug(msg);
				return new CheckResultImpl(this, true, msg);
			}
		}
		catch (final MalformedURLException e) {
			logger.warn("MalformedURLException", e);
			return new CheckResultImpl(this, false, "MalformedURLException");
		}
		catch (final IOException e) {
			logger.warn("IOException", e);
			return new CheckResultImpl(this, false, "IOException");
		}
	}

	private String getRow(final String content) {
		final String startString = "<tr><td data=\"";
		final String endString = "</tr>";
		final int pos = content.indexOf(">" + job + "</a></td><td data=\"");
		final int start = content.lastIndexOf(startString, pos);
		final int end = content.indexOf(endString, pos);
		if (start != -1 && end != -1) {
			return content.substring(start + startString.length(), end);
		}
		else {
			return null;
		}
	}

}
