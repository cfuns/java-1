package de.benjaminborbe.crawler.service;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.crawler.api.CrawlerInstruction;
import de.benjaminborbe.crawler.api.CrawlerService;
import de.benjaminborbe.crawler.api.CrawlerException;
import de.benjaminborbe.crawler.api.CrawlerNotifier;
import de.benjaminborbe.crawler.api.CrawlerResult;
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

	protected void crawleDomain(final String domainUrl) throws CrawlerException {
		try {
			logger.debug("crawle domain: " + domainUrl);
			final HttpDownloadResult result = httpDownloader.downloadUrlUnsecure(new URL(domainUrl), TIMEOUT);
			final String content = httpDownloadUtil.getContent(result);
			final CrawlerResult crawleResult = new CrawlerResultImpl(domainUrl, content);
			crawlerNotifier.notifiy(crawleResult);
		}
		catch (final MalformedURLException e) {
			throw new CrawlerException("MalformedURLException", e);
		}
		catch (final HttpDownloaderException e) {
			throw new CrawlerException("MalformedURLException", e);
		}
		catch (final UnsupportedEncodingException e) {
			throw new CrawlerException("MalformedURLException", e);
		}
	}

	@Override
	public void processCrawlerInstruction(final CrawlerInstruction crawlerInstruction) throws CrawlerException {
		crawleDomain(crawlerInstruction.getUrl());
	}

}
