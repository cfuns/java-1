package de.benjaminborbe.httpdownloader.core.service;

import de.benjaminborbe.httpdownloader.api.HttpRequest;
import de.benjaminborbe.httpdownloader.api.HttpResponse;
import de.benjaminborbe.httpdownloader.api.HttpdownloaderService;
import de.benjaminborbe.httpdownloader.api.HttpdownloaderServiceException;
import de.benjaminborbe.httpdownloader.core.util.HttpdownloaderGet;
import de.benjaminborbe.httpdownloader.core.util.HttpdownloaderGetSecure;
import de.benjaminborbe.httpdownloader.core.util.HttpdownloaderGetUnsecure;
import de.benjaminborbe.httpdownloader.tools.HttpContentByteArray;
import de.benjaminborbe.httpdownloader.tools.HttpHeaderDto;
import de.benjaminborbe.httpdownloader.tools.HttpResponseDto;
import de.benjaminborbe.tools.http.HttpDownloadResult;
import de.benjaminborbe.tools.http.HttpDownloaderException;
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

	@Inject
	public HttpdownloaderCoreServiceImpl(
		final Logger logger,
		final HttpdownloaderGetSecure httpdownloaderGetSecure,
		final HttpdownloaderGetUnsecure httpdownloaderGetUnsecure
	) {
		this.logger = logger;
		this.httpdownloaderGetSecure = httpdownloaderGetSecure;
		this.httpdownloaderGetUnsecure = httpdownloaderGetUnsecure;
	}

	public HttpResponse getUnsecure(final HttpRequest httpRequest) throws HttpdownloaderServiceException {
		return download(httpRequest, httpdownloaderGetUnsecure);
	}

	@Override
	public HttpResponse getSecure(final HttpRequest httpRequest) throws HttpdownloaderServiceException {
		return download(httpRequest, httpdownloaderGetSecure);
	}

	private HttpResponse download(final HttpRequest httpRequest, final HttpdownloaderGet httpdownloaderGet) throws HttpdownloaderServiceException {
		try {
			final URL url = httpRequest.getUrl();
			logger.debug("download url: " + url);
			final HttpDownloadResult httpDownloadResult = httpdownloaderGet.getUrl(url, httpRequest.getTimeout());

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

