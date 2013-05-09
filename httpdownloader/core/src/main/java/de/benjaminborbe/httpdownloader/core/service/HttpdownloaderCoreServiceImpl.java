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
import de.benjaminborbe.httpdownloader.tools.HttpResponseDto;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.net.URL;
import java.util.List;
import java.util.Map;

@Singleton
public class HttpdownloaderCoreServiceImpl implements HttpdownloaderService {

	private final Logger logger;

	private final HttpdownloaderGetSecure httpdownloaderGetSecure;

	private final HttpdownloaderGetUnsecure httpdownloaderGetUnsecure;

	private final HttpdownloaderPostSecure httpdownloaderPostSecure;

	private final HttpdownloaderPostUnsecure httpdownloaderPostUnsecure;

	@Inject
	public HttpdownloaderCoreServiceImpl(
		final Logger logger,
		final HttpdownloaderGetSecure httpdownloaderGetSecure,
		final HttpdownloaderGetUnsecure httpdownloaderGetUnsecure,
		final HttpdownloaderPostSecure httpdownloaderPostSecure,
		final HttpdownloaderPostUnsecure httpdownloaderPostUnsecure
	) {
		this.logger = logger;
		this.httpdownloaderGetSecure = httpdownloaderGetSecure;
		this.httpdownloaderGetUnsecure = httpdownloaderGetUnsecure;
		this.httpdownloaderPostSecure = httpdownloaderPostSecure;
		this.httpdownloaderPostUnsecure = httpdownloaderPostUnsecure;
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
		try {
			final URL url = httpRequest.getUrl();
			logger.debug("download url: " + url);
			final HttpDownloadResult httpDownloadResult = httpdownloader.fetch(httpRequest);

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

