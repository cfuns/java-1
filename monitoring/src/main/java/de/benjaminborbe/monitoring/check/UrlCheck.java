package de.benjaminborbe.monitoring.check;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
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

	private final String titleMatch;

	private final String contentMatch;

	public UrlCheck(
			final Logger logger,
			final HttpDownloader httpDownloader,
			final String urlString,
			final String titleMatch,
			final String contentMatch) {
		this.logger = logger;
		this.httpDownloader = httpDownloader;
		this.urlString = urlString;
		this.titleMatch = titleMatch;
		this.contentMatch = contentMatch;
	}

	@Override
	public boolean check() {
		try {
			final URL url = new URL(urlString);
			final HttpDownloadResult result = httpDownloader.downloadUrlUnsecure(url, TIMEOUT);
			logger.debug("downloaded " + url + " in " + result.getDuration() + " ms");
			if (result.getDuration() > TIMEOUT) {
				logger.warn("downloaded " + url + " slow");
				return false;
			}
			final String content = getContent(result);
			if (!checkTitle(content)) {
				logger.warn("cannot find title " + titleMatch + " in content of " + url);
				return false;
			}
			if (!checkContent(content)) {
				logger.warn("cannot find string " + contentMatch + " in content of " + url);
				return false;
			}
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

	protected String getContent(final HttpDownloadResult result) throws UnsupportedEncodingException {
		if (result.getContent() == null) {
			return null;
		}
		else if (result.getContentEncoding() != null && result.getContentEncoding().getEncoding() != null) {
			return new String(result.getContent(), result.getContentEncoding().getEncoding());
		}
		else {
			return new String(result.getContent());
		}
	}

	protected boolean checkContent(final String content) {
		return contentMatch == null || content.indexOf(contentMatch) != -1;
	}

	protected boolean checkTitle(final String content) {
		return titleMatch == null || content.indexOf("<title>" + titleMatch + "</title>") != -1;
	}

	@Override
	public String getMessage() {
		return "check url: " + urlString + " titleMatch: " + titleMatch + " contentMatch: " + contentMatch;
	}

}
