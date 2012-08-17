package de.benjaminborbe.dhl.util;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.dhl.api.DhlIdentifier;
import de.benjaminborbe.tools.http.HttpDownloadResult;
import de.benjaminborbe.tools.http.HttpDownloadUtil;
import de.benjaminborbe.tools.http.HttpDownloader;
import de.benjaminborbe.tools.http.HttpDownloaderException;

@Singleton
public class DhlStatusFetcherImpl implements DhlStatusFetcher {

	private static final int TIMEOUT = 5000;

	private final Logger logger;

	private final HttpDownloader httpDownloader;

	private final HttpDownloadUtil httpDownloadUtil;

	private final DhlStatusParser dhlStatusParser;

	private final DhlUrlBuilder dhlUrlBuilder;

	@Inject
	public DhlStatusFetcherImpl(
			final Logger logger,
			final HttpDownloader httpDownloader,
			final HttpDownloadUtil httpDownloadUtil,
			final DhlStatusParser dhlStatusParser,
			final DhlUrlBuilder dhlUrlBuilder) {
		this.logger = logger;
		this.httpDownloader = httpDownloader;
		this.httpDownloadUtil = httpDownloadUtil;
		this.dhlStatusParser = dhlStatusParser;
		this.dhlUrlBuilder = dhlUrlBuilder;
	}

	@Override
	public DhlStatus fetchStatus(final DhlIdentifier dhlIdentifier) throws DhlStatusFetcherException {
		logger.debug("getStatus for " + dhlIdentifier);

		try {
			final URL url = dhlUrlBuilder.buildUrl(dhlIdentifier);
			final HttpDownloadResult result = httpDownloader.getUrlUnsecure(url, TIMEOUT);
			final String content = httpDownloadUtil.getContent(result);
			return dhlStatusParser.parseCurrentStatus(dhlIdentifier, content);
		}
		catch (final MalformedURLException e) {
			throw new DhlStatusFetcherException("MalformedURLException", e);
		}
		catch (final HttpDownloaderException e) {
			throw new DhlStatusFetcherException("HttpDownloaderException", e);
		}
		catch (final UnsupportedEncodingException e) {
			throw new DhlStatusFetcherException("UnsupportedEncodingException", e);
		}
	}

}
