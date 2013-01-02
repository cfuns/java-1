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
import de.benjaminborbe.tools.util.ParseException;
import de.benjaminborbe.tools.util.ParseUtil;

@Singleton
public class CrawlerServiceImpl implements CrawlerService {

	private final Logger logger;

	private final HttpDownloader httpDownloader;

	private final HttpDownloadUtil httpDownloadUtil;

	private final CrawlerNotifier crawlerNotifier;

	private final ParseUtil parseUtil;

	@Inject
	public CrawlerServiceImpl(
			final Logger logger,
			final ParseUtil parseUtil,
			final HttpDownloader httpDownloader,
			final HttpDownloadUtil httpDownloadUtil,
			final CrawlerNotifier crawlerNotifier) {
		this.logger = logger;
		this.parseUtil = parseUtil;
		this.httpDownloader = httpDownloader;
		this.httpDownloadUtil = httpDownloadUtil;
		this.crawlerNotifier = crawlerNotifier;
	}

	protected void crawleDomain(final URL url, final int timeout) throws CrawlerException {
		try {
			logger.trace("crawle domain: " + url);
			final HttpDownloadResult result = httpDownloader.getUrlUnsecure(url, timeout);
			final String content = httpDownloadUtil.getContent(result);
			final String contentType = result.getContentType();
			crawlerNotifier.notifiy(new CrawlerResultImpl(url, content, contentType, true));
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

	@Override
	public void processCrawlerInstruction(final CrawlerInstruction crawlerInstruction) throws CrawlerException {
		logger.trace("processCrawlerInstruction");
		try {
			crawleDomain(parseUtil.parseURL(crawlerInstruction.getUrl()), crawlerInstruction.getTimeout());
		}
		catch (final ParseException e) {
			throw new CrawlerException(e);
		}
	}

}
