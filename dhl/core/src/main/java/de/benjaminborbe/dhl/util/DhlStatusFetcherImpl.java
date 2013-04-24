package de.benjaminborbe.dhl.util;

import de.benjaminborbe.dhl.api.Dhl;
import de.benjaminborbe.tools.http.HttpDownloadResult;
import de.benjaminborbe.tools.http.HttpDownloadUtil;
import de.benjaminborbe.tools.http.HttpDownloader;
import de.benjaminborbe.tools.http.HttpDownloaderException;
import de.benjaminborbe.tools.util.ParseException;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.UnsupportedEncodingException;
import java.net.URL;

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
	public DhlStatus fetchStatus(final Dhl dhl) throws DhlStatusFetcherException {
		try {
			logger.debug("getStatus for " + dhl.getTrackingNumber());
			final URL url = dhlUrlBuilder.buildUrl(dhl);
			final HttpDownloadResult result = httpDownloader.getUrlUnsecure(url, TIMEOUT);
			final String content = httpDownloadUtil.getContent(result);
			return dhlStatusParser.parseCurrentStatus(dhl, content);
		} catch (final ParseException e) {
			throw new DhlStatusFetcherException("MalformedURLException", e);
		} catch (final HttpDownloaderException e) {
			throw new DhlStatusFetcherException("HttpDownloaderException", e);
		} catch (final UnsupportedEncodingException e) {
			throw new DhlStatusFetcherException("UnsupportedEncodingException", e);
		}
	}

}
