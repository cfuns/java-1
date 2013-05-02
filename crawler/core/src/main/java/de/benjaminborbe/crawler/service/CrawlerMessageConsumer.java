package de.benjaminborbe.crawler.service;

import de.benjaminborbe.analytics.api.AnalyticsReportIdentifier;
import de.benjaminborbe.analytics.api.AnalyticsService;
import de.benjaminborbe.crawler.CrawlerConstants;
import de.benjaminborbe.crawler.api.CrawlerException;
import de.benjaminborbe.crawler.api.CrawlerNotifier;
import de.benjaminborbe.crawler.message.CrawlerMessage;
import de.benjaminborbe.crawler.message.CrawlerMessageMapper;
import de.benjaminborbe.httpdownloader.api.HttpRequestDto;
import de.benjaminborbe.httpdownloader.api.HttpResponse;
import de.benjaminborbe.httpdownloader.api.HttpUtil;
import de.benjaminborbe.httpdownloader.api.HttpdownloaderService;
import de.benjaminborbe.httpdownloader.api.HttpdownloaderServiceException;
import de.benjaminborbe.message.api.Message;
import de.benjaminborbe.message.api.MessageConsumer;
import de.benjaminborbe.tools.mapper.MapException;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.net.URL;

@Singleton
public class CrawlerMessageConsumer implements MessageConsumer {

	private final Logger logger;

	private final CrawlerNotifier crawlerNotifier;

	private final CrawlerMessageMapper crawlerMessageMapper;

	private final AnalyticsService analyticsService;

	private final HttpUtil httpUtil;

	private final HttpdownloaderService httpdownloaderService;

	private final AnalyticsReportIdentifier analyticsReportIdentifier = new AnalyticsReportIdentifier("CrawlerHttpRequestCounter");

	@Inject
	public CrawlerMessageConsumer(
		final Logger logger,
		final AnalyticsService analyticsService,
		final HttpUtil httpUtil,
		final HttpdownloaderService httpdownloaderService,
		final CrawlerNotifier crawlerNotifier,
		final CrawlerMessageMapper crawlerMessageMapper
	) {
		this.logger = logger;
		this.analyticsService = analyticsService;
		this.httpUtil = httpUtil;
		this.httpdownloaderService = httpdownloaderService;
		this.crawlerNotifier = crawlerNotifier;
		this.crawlerMessageMapper = crawlerMessageMapper;
	}

	protected void crawleDomain(final URL url, final int timeout) throws CrawlerException {
		try {
			logger.trace("crawle domain: " + url + " started");

			final HttpRequestDto httpRequestDto = new HttpRequestDto();
			httpRequestDto.setUrl(url);
			httpRequestDto.setTimeout(timeout);

			final HttpResponse httpResponse = httpdownloaderService.get(httpRequestDto);

			logger.debug("url: " + url + " contentType: " + httpUtil.getContentType(httpResponse.getHeader()) + " returnCode: " + httpResponse.getReturnCode());
			crawlerNotifier.notifiy(httpResponse);
			logger.trace("crawle domain: " + url + " finished");
			track();
		} catch (HttpdownloaderServiceException e) {
			throw new CrawlerException(e);
		}
	}

	private void track() {
		try {
			analyticsService.addReportValue(analyticsReportIdentifier);
		} catch (final Exception e) {
			logger.warn(e.getClass().getName(), e);
		}
	}

	@Override
	public String getType() {
		return CrawlerConstants.MESSSAGE_TYPE;
	}

	@Override
	public boolean process(final Message message) {
		try {
			final CrawlerMessage crawlerMessage = crawlerMessageMapper.map(message.getContent());
			crawleDomain(crawlerMessage.getUrl(), crawlerMessage.getTimeout());
			return true;
		} catch (final CrawlerException | MapException e) {
			logger.warn("process crawlerMessage failed!", e);
			return false;
		}
	}
}
