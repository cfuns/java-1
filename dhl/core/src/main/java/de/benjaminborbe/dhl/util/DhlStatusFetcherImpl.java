package de.benjaminborbe.dhl.util;

import de.benjaminborbe.dhl.api.Dhl;
import de.benjaminborbe.httpdownloader.api.HttpResponse;
import de.benjaminborbe.httpdownloader.api.HttpdownloaderService;
import de.benjaminborbe.httpdownloader.api.HttpdownloaderServiceException;
import de.benjaminborbe.httpdownloader.tools.HttpRequestBuilder;
import de.benjaminborbe.httpdownloader.tools.HttpUtil;
import de.benjaminborbe.tools.util.ParseException;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.net.URL;

@Singleton
public class DhlStatusFetcherImpl implements DhlStatusFetcher {

	private static final int TIMEOUT = 5000;

	private final Logger logger;

	private final HttpdownloaderService httpdownloaderService;

	private final HttpUtil httpUtil;

	private final DhlStatusParser dhlStatusParser;

	private final DhlUrlBuilder dhlUrlBuilder;

	@Inject
	public DhlStatusFetcherImpl(
		final Logger logger,
		final HttpdownloaderService httpdownloaderService,
		final HttpUtil httpUtil,
		final DhlStatusParser dhlStatusParser,
		final DhlUrlBuilder dhlUrlBuilder
	) {
		this.logger = logger;
		this.httpdownloaderService = httpdownloaderService;
		this.httpUtil = httpUtil;
		this.dhlStatusParser = dhlStatusParser;
		this.dhlUrlBuilder = dhlUrlBuilder;
	}

	@Override
	public DhlStatus fetchStatus(final Dhl dhl) throws DhlStatusFetcherException {
		try {
			logger.debug("getStatus for " + dhl.getTrackingNumber());
			final URL url = dhlUrlBuilder.buildUrl(dhl);
			final HttpResponse httpResponse = httpdownloaderService.fetch(new HttpRequestBuilder(url).addTimeout(TIMEOUT).build());
			final String content = httpUtil.getContent(httpResponse);
			return dhlStatusParser.parseCurrentStatus(dhl, content);
		} catch (IOException e) {
			throw new DhlStatusFetcherException("fetch dlh status failed!", e);
		} catch (HttpdownloaderServiceException e) {
			throw new DhlStatusFetcherException("fetch dlh status failed!", e);
		} catch (ParseException e) {
			throw new DhlStatusFetcherException("fetch dlh status failed!", e);
		}
	}

}
