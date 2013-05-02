package de.benjaminborbe.proxy.core.util;

import de.benjaminborbe.crawler.api.CrawlerException;
import de.benjaminborbe.crawler.api.CrawlerService;
import de.benjaminborbe.httpdownloader.api.HttpContentByteArray;
import de.benjaminborbe.httpdownloader.api.HttpHeaderDto;
import de.benjaminborbe.httpdownloader.api.HttpResponse;
import de.benjaminborbe.httpdownloader.api.HttpResponseDto;
import de.benjaminborbe.proxy.api.ProxyConversation;
import de.benjaminborbe.tools.util.ParseException;

import javax.inject.Inject;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class ProxyConversationListenerCrawler implements ProxyConversationListener {

	private final CrawlerService crawlerService;

	private final ProxyLineReader proxyLineReader;

	private final ProxyLineParser proxyLineParser;

	@Inject
	public ProxyConversationListenerCrawler(final CrawlerService crawlerService, final ProxyLineReader proxyLineReader, final ProxyLineParser proxyLineParser) {
		this.crawlerService = crawlerService;
		this.proxyLineReader = proxyLineReader;
		this.proxyLineParser = proxyLineParser;
	}

	@Override
	public void onProxyConversationCompleted(final ProxyConversation proxyConversation) throws CrawlerException, IOException, ParseException {
		crawlerService.notify(buildHttpResponse(proxyConversation));
	}

	private HttpResponse buildHttpResponse(final ProxyConversation proxyConversation) throws IOException, ParseException {

		final byte[] content = proxyConversation.getResponse().getContent();
		ByteArrayInputStream inputStream = new ByteArrayInputStream(content);

		HttpHeaderDto httpHeaderDto = new HttpHeaderDto();
		String line = proxyLineReader.readLine(inputStream);
		while (line != null && !line.isEmpty()) {
			proxyLineParser.parseHeaderLine(line);
			line = proxyLineReader.readLine(inputStream);
		}

		final HttpResponseDto httpResponseDto = new HttpResponseDto();
		httpResponseDto.setUrl(proxyConversation.getUrl());
		httpResponseDto.setDuration(proxyConversation.getDuration());
		httpResponseDto.setContent(new HttpContentByteArray(content));
		httpResponseDto.setHeader(httpHeaderDto);
		httpResponseDto.setReturnCode(proxyLineParser.parseReturnCode(proxyLineReader.readLine(inputStream)));

		return httpResponseDto;
	}
}
