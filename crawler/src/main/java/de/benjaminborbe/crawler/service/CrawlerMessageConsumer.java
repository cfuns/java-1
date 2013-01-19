package de.benjaminborbe.crawler.service;

import java.io.UnsupportedEncodingException;
import java.net.URL;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.analytics.api.AnalyticsReportIdentifier;
import de.benjaminborbe.analytics.api.AnalyticsService;
import de.benjaminborbe.crawler.CrawlerConstants;
import de.benjaminborbe.crawler.api.CrawlerException;
import de.benjaminborbe.crawler.api.CrawlerNotifier;
import de.benjaminborbe.crawler.message.CrawlerMessage;
import de.benjaminborbe.crawler.message.CrawlerMessageMapper;
import de.benjaminborbe.message.api.Message;
import de.benjaminborbe.message.api.MessageConsumer;
import de.benjaminborbe.tools.http.HttpDownloadResult;
import de.benjaminborbe.tools.http.HttpDownloadUtil;
import de.benjaminborbe.tools.http.HttpDownloader;
import de.benjaminborbe.tools.http.HttpDownloaderException;
import de.benjaminborbe.tools.mapper.MapException;

@Singleton
public class CrawlerMessageConsumer implements MessageConsumer {

	private final Logger logger;

	private final HttpDownloader httpDownloader;

	private final HttpDownloadUtil httpDownloadUtil;

	private final CrawlerNotifier crawlerNotifier;

	private final CrawlerMessageMapper crawlerMessageMapper;

	private final AnalyticsService analyticsService;

	private final AnalyticsReportIdentifier analyticsReportIdentifier = new AnalyticsReportIdentifier("CrawlerHttpRequestCounter");

	@Inject
	public CrawlerMessageConsumer(
			final Logger logger,
			final AnalyticsService analyticsService,
			final HttpDownloader httpDownloader,
			final HttpDownloadUtil httpDownloadUtil,
			final CrawlerNotifier crawlerNotifier,
			final CrawlerMessageMapper crawlerMessageMapper) {
		this.logger = logger;
		this.analyticsService = analyticsService;
		this.httpDownloader = httpDownloader;
		this.httpDownloadUtil = httpDownloadUtil;
		this.crawlerNotifier = crawlerNotifier;
		this.crawlerMessageMapper = crawlerMessageMapper;
	}

	protected void crawleDomain(final URL url, final int timeout) throws CrawlerException {
		try {
			logger.debug("crawle domain: " + url);
			final HttpDownloadResult result = httpDownloader.getUrlUnsecure(url, timeout);
			final String content = httpDownloadUtil.getContent(result);
			final String contentType = result.getContentType();
			crawlerNotifier.notifiy(new CrawlerResultImpl(url, content, contentType, true));
			track();
		}
		catch (final HttpDownloaderException e) {
			logger.trace("HttpDownloaderException url: " + url, e);
			logger.info("HttpDownloaderException url: " + url);
			crawlerNotifier.notifiy(new CrawlerResultImpl(url, null, null, false));
		}
		catch (final UnsupportedEncodingException e) {
			logger.warn("UnsupportedEncodingException url: " + url, e);
			crawlerNotifier.notifiy(new CrawlerResultImpl(url, null, null, false));
		}
	}

	private void track() {
		try {
			analyticsService.addReportValue(analyticsReportIdentifier);
		}
		catch (final Exception e) {
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
		}
		catch (final MapException e) {
			logger.warn(e.getClass().getName());
			return false;
		}
		catch (final CrawlerException e) {
			logger.warn(e.getClass().getName());
			return false;
		}
	}
}
