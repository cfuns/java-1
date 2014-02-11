package de.benjaminborbe.httpdownloader.core.service;

import de.benjaminborbe.httpdownloader.api.HttpMethod;
import de.benjaminborbe.httpdownloader.api.HttpRequest;
import de.benjaminborbe.httpdownloader.api.HttpResponse;
import de.benjaminborbe.httpdownloader.api.HttpdownloaderService;
import de.benjaminborbe.httpdownloader.api.HttpdownloaderServiceException;
import de.benjaminborbe.httpdownloader.core.util.HttpDownloadResult;
import de.benjaminborbe.httpdownloader.core.util.HttpDownloaderException;
import de.benjaminborbe.httpdownloader.core.util.HttpdownloaderAction;
import de.benjaminborbe.httpdownloader.core.util.HttpdownloaderGetSecure;
import de.benjaminborbe.httpdownloader.core.util.HttpdownloaderGetUnsecure;
import de.benjaminborbe.httpdownloader.core.util.HttpdownloaderPostSecure;
import de.benjaminborbe.httpdownloader.core.util.HttpdownloaderPostUnsecure;
import de.benjaminborbe.httpdownloader.tools.HttpContentByteArray;
import de.benjaminborbe.httpdownloader.tools.HttpHeaderDto;
import de.benjaminborbe.httpdownloader.tools.HttpRequestBuilder;
import de.benjaminborbe.httpdownloader.tools.HttpResponseDto;
import de.benjaminborbe.tools.util.ParseException;
import de.benjaminborbe.tools.util.ParseUtil;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.net.URL;
import java.util.List;
import java.util.Map;

@Singleton
public class HttpdownloaderCoreServiceImpl implements HttpdownloaderService {

	public static final String LOCATION_HEADER_FIELD = "Location";

	private static final int MAX_FOLLOW_REDIRECTS = 10;

	private final Logger logger;

	private final HttpdownloaderGetSecure httpdownloaderGetSecure;

	private final HttpdownloaderGetUnsecure httpdownloaderGetUnsecure;

	private final HttpdownloaderPostSecure httpdownloaderPostSecure;

	private final HttpdownloaderPostUnsecure httpdownloaderPostUnsecure;

	private final ParseUtil parseUtil;

	@Inject
	public HttpdownloaderCoreServiceImpl(
		final Logger logger,
		final HttpdownloaderGetSecure httpdownloaderGetSecure,
		final HttpdownloaderGetUnsecure httpdownloaderGetUnsecure,
		final HttpdownloaderPostSecure httpdownloaderPostSecure,
		final HttpdownloaderPostUnsecure httpdownloaderPostUnsecure,
		final ParseUtil parseUtil
	) {
		this.logger = logger;
		this.httpdownloaderGetSecure = httpdownloaderGetSecure;
		this.httpdownloaderGetUnsecure = httpdownloaderGetUnsecure;
		this.httpdownloaderPostSecure = httpdownloaderPostSecure;
		this.httpdownloaderPostUnsecure = httpdownloaderPostUnsecure;
		this.parseUtil = parseUtil;
	}

	@Override
	public HttpResponse fetch(final HttpRequest httpRequest) throws HttpdownloaderServiceException {
		final HttpdownloaderAction downloader;
		if (HttpMethod.POST.equals(httpRequest.getHttpMethod())) {
			if (Boolean.FALSE.equals(httpRequest.getSecure())) {
				downloader = httpdownloaderPostUnsecure;
			} else {
				downloader = httpdownloaderPostSecure;
			}
		} else {
			if (Boolean.FALSE.equals(httpRequest.getSecure())) {
				downloader = httpdownloaderGetUnsecure;
			} else {
				downloader = httpdownloaderGetSecure;
			}
		}
		return download(httpRequest, downloader);
	}

	private HttpResponse download(final HttpRequest httpRequest, final HttpdownloaderAction httpdownloader) throws HttpdownloaderServiceException {
		return download(httpRequest, httpdownloader, 0);
	}

	private HttpResponse download(
		final HttpRequest httpRequest,
		final HttpdownloaderAction httpdownloader,
		final int redirectCounter
	) throws HttpdownloaderServiceException {
		if (redirectCounter > MAX_FOLLOW_REDIRECTS) {
			throw new HttpdownloaderServiceException("max follow redirects (" + MAX_FOLLOW_REDIRECTS + ") reached");
		}
		try {
			final URL url = httpRequest.getUrl();
			logger.debug("download url: '" + url + "'");
			final HttpDownloadResult httpDownloadResult = httpdownloader.fetch(httpRequest);

			// follow redirects
			if (Boolean.TRUE.equals(httpRequest.getFollowRedirects())) {
				logger.trace("following redirects enabled");
				if ((httpDownloadResult.getResponseCode() / 100) == 3) {
					logger.trace("return is code 3xx");
					final List<String> locations = httpDownloadResult.getHeaders().get(LOCATION_HEADER_FIELD);
					if (locations != null) {
						logger.trace("found " + locations.size() + locations);
						for (final String location : locations) {
							try {
								final URL locationUrl = parseUtil.parseURL(location);
								final HttpRequestBuilder httpRequestBuilder = new HttpRequestBuilder(locationUrl).copyRequest(httpRequest);
								return download(httpRequestBuilder.build(), httpdownloader, redirectCounter + 1);
							} catch (ParseException e) {
								logger.trace("illegal location to redirect to: " + location, e);
							}
						}
					}
					logger.warn("redirect returnCode, but valid location found");
				}
			} else {
				logger.trace("following redirects disabled");
			}

			final HttpResponseDto httpResponse = new HttpResponseDto();
			httpResponse.setReturnCode(httpDownloadResult.getResponseCode());
			httpResponse.setUrl(url);
			httpResponse.setDuration(httpDownloadResult.getDuration());
			httpResponse.setContent(new HttpContentByteArray(httpDownloadResult.getContent()));
			final HttpHeaderDto header = new HttpHeaderDto();
			if (httpDownloadResult.getHeaders() != null) {
				for (final Map.Entry<String, List<String>> e : httpDownloadResult.getHeaders().entrySet()) {
					header.setHeader(e.getKey(), e.getValue());
				}
			} else {
				logger.trace("no header found");
			}
			httpResponse.setHeader(header);
			return httpResponse;
		} catch (HttpDownloaderException e) {
			throw new HttpdownloaderServiceException(e);
		}
	}
}

