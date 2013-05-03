package de.benjaminborbe.proxy.core.util;

import de.benjaminborbe.crawler.api.CrawlerException;
import de.benjaminborbe.crawler.api.CrawlerService;
import de.benjaminborbe.httpdownloader.api.HttpContentByteArray;
import de.benjaminborbe.httpdownloader.api.HttpHeaderDto;
import de.benjaminborbe.httpdownloader.api.HttpResponse;
import de.benjaminborbe.httpdownloader.api.HttpResponseDto;
import de.benjaminborbe.proxy.api.ProxyConversation;
import de.benjaminborbe.proxy.core.config.ProxyCoreConfig;
import de.benjaminborbe.tools.stream.StreamUtil;
import de.benjaminborbe.tools.util.ParseException;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class ProxyConversationListenerCrawler implements ProxyConversationListener {

	private final Logger logger;

	private final CrawlerService crawlerService;

	private final ProxyLineReader proxyLineReader;

	private final ProxyLineParser proxyLineParser;

	private final StreamUtil streamUtil;

	private final ProxyCoreConfig proxyCoreConfig;

	@Inject
	public ProxyConversationListenerCrawler(
		final Logger logger,
		final CrawlerService crawlerService,
		final ProxyLineReader proxyLineReader,
		final ProxyLineParser proxyLineParser,
		final StreamUtil streamUtil,
		final ProxyCoreConfig proxyCoreConfig
	) {
		this.logger = logger;
		this.crawlerService = crawlerService;
		this.proxyLineReader = proxyLineReader;
		this.proxyLineParser = proxyLineParser;
		this.streamUtil = streamUtil;
		this.proxyCoreConfig = proxyCoreConfig;
	}

	@Override
	public void onProxyConversationCompleted(final ProxyConversation proxyConversation) throws CrawlerException, IOException, ParseException {
		if (proxyCoreConfig.conversationCrawler()) {
			logger.debug("add proxy request to crawler");
			crawlerService.notify(buildHttpResponse(proxyConversation));
		} else {
			logger.trace("skip");
		}
	}

	private HttpResponse buildHttpResponse(final ProxyConversation proxyConversation) throws IOException, ParseException {

		final byte[] content = proxyConversation.getResponse().getContent();
		final ByteArrayInputStream inputStream = new ByteArrayInputStream(content);
		final int returnCode = proxyLineParser.parseReturnCode(proxyLineReader.readLine(inputStream));

		final HttpHeaderDto httpHeaderDto = new HttpHeaderDto();
		String line = proxyLineReader.readLine(inputStream);
		while (line != null && !line.trim().isEmpty()) {
			final Map<String, List<String>> headers = proxyLineParser.parseHeaderLine(line.trim());
			for (final Map.Entry<String, List<String>> header : headers.entrySet()) {
				httpHeaderDto.setHeader(header.getKey(), header.getValue());
			}
			line = proxyLineReader.readLine(inputStream);
		}

		final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		streamUtil.copy(inputStream, outputStream);

		final HttpResponseDto httpResponseDto = new HttpResponseDto();
		httpResponseDto.setUrl(proxyConversation.getUrl());
		httpResponseDto.setDuration(proxyConversation.getDuration());
		httpResponseDto.setContent(new HttpContentByteArray(outputStream.toByteArray()));
		httpResponseDto.setHeader(httpHeaderDto);
		httpResponseDto.setReturnCode(returnCode);

		return httpResponseDto;
	}
}
