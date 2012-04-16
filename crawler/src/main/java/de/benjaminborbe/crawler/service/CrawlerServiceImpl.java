package de.benjaminborbe.crawler.service;

import java.io.UnsupportedEncodingException;
import java.net.URL;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.crawler.api.CrawlerException;
import de.benjaminborbe.crawler.api.CrawlerInstruction;
import de.benjaminborbe.crawler.api.CrawlerNotifier;
import de.benjaminborbe.crawler.api.CrawlerService;
import de.benjaminborbe.tools.http.HttpDownloadResult;
import de.benjaminborbe.tools.http.HttpDownloadUtil;
import de.benjaminborbe.tools.http.HttpDownloader;
import de.benjaminborbe.tools.http.HttpDownloaderException;

@Singleton
public class CrawlerServiceImpl implements CrawlerService {

	private final Logger logger;

	// 5 sec
	private static final int TIMEOUT = 5 * 1000;

	private final HttpDownloader httpDownloader;

	private final HttpDownloadUtil httpDownloadUtil;

	private final CrawlerNotifier crawlerNotifier;

	@Inject
	public CrawlerServiceImpl(final Logger logger, final HttpDownloader httpDownloader, final HttpDownloadUtil httpDownloadUtil, final CrawlerNotifier crawlerNotifier) {
		this.logger = logger;
		this.httpDownloader = httpDownloader;
		this.httpDownloadUtil = httpDownloadUtil;
		this.crawlerNotifier = crawlerNotifier;
	}

	protected void crawleDomain(final URL domainUrl) throws CrawlerException {
		try {
			logger.debug("crawle domain: " + domainUrl);
			final HttpDownloadResult result = httpDownloader.downloadUrlUnsecure(domainUrl, TIMEOUT);
			final String content = httpDownloadUtil.getContent(result);
			final String contentType = result.getContentType();
			crawlerNotifier.notifiy(new CrawlerResultImpl(domainUrl, content, contentType, true));
		}
		catch (final HttpDownloaderException e) {
			logger.warn("HttpDownloaderException url: " + domainUrl, e);
			crawlerNotifier.notifiy(new CrawlerResultImpl(domainUrl, null, null, false));
		}
		catch (final UnsupportedEncodingException e) {
			logger.warn("UnsupportedEncodingException url: " + domainUrl, e);
			crawlerNotifier.notifiy(new CrawlerResultImpl(domainUrl, null, null, false));
		}
	}

	@Override
	public void processCrawlerInstruction(final CrawlerInstruction crawlerInstruction) throws CrawlerException {
		logger.debug("processCrawlerInstruction");
		crawleDomain(crawlerInstruction.getUrl());
	}

}
