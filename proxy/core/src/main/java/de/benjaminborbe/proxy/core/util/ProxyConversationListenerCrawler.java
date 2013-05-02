package de.benjaminborbe.proxy.core.util;

import de.benjaminborbe.crawler.api.CrawlerException;
import de.benjaminborbe.crawler.api.CrawlerService;
import de.benjaminborbe.httpdownloader.api.HttpResponse;
import de.benjaminborbe.httpdownloader.api.HttpResponseDto;
import de.benjaminborbe.proxy.api.ProxyConversation;

import javax.inject.Inject;

public class ProxyConversationListenerCrawler implements ProxyConversationListener {

	private final CrawlerService crawlerService;

	@Inject
	public ProxyConversationListenerCrawler(final CrawlerService crawlerService) {
		this.crawlerService = crawlerService;
	}

	@Override
	public void onProxyConversationCompleted(final ProxyConversation proxyConversation) throws CrawlerException {
		crawlerService.notify(buildHttpResponse(proxyConversation));
	}

	private HttpResponse buildHttpResponse(final ProxyConversation proxyConversation) {
		final HttpResponseDto httpResponseDto = new HttpResponseDto();
		httpResponseDto.setUrl(null);
		return httpResponseDto;
	}
}
