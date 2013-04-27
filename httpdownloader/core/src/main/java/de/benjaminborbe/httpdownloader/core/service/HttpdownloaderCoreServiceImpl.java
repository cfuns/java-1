package de.benjaminborbe.httpdownloader.core.service;

import de.benjaminborbe.httpdownloader.api.HttpHeaderDto;
import de.benjaminborbe.httpdownloader.api.HttpRequest;
import de.benjaminborbe.httpdownloader.api.HttpResponse;
import de.benjaminborbe.httpdownloader.api.HttpResponseDto;
import de.benjaminborbe.httpdownloader.api.HttpdownloaderService;
import de.benjaminborbe.httpdownloader.api.HttpdownloaderServiceException;
import de.benjaminborbe.httpdownloader.core.util.HttpContentByteArray;
import de.benjaminborbe.tools.http.HttpDownloadResult;
import de.benjaminborbe.tools.http.HttpDownloader;
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

	private final HttpDownloader httpDownloader;

	@Inject
	public HttpdownloaderCoreServiceImpl(final Logger logger, final HttpDownloader httpDownloader) {
		this.logger = logger;
		this.httpDownloader = httpDownloader;
	}

	@Override
	public HttpResponse get(final HttpRequest httpRequest) throws HttpdownloaderServiceException {
		try {
			logger.debug("execute");
			final URL url = httpRequest.getUrl();
			final HttpDownloadResult httpDownloadResult = httpDownloader.getUrl(url, httpRequest.getTimeout());

			HttpResponseDto httpResponse = new HttpResponseDto();
			httpResponse.setReturnCode(httpDownloadResult.getResponseCode());
			httpResponse.setUrl(url);
			httpResponse.setDuration(httpDownloadResult.getDuration());
			httpResponse.setContent(new HttpContentByteArray(httpDownloadResult.getContent()));
			final HttpHeaderDto header = new HttpHeaderDto();
			for (Map.Entry<String, List<String>> e : httpDownloadResult.getHeaders().entrySet()) {
				header.setHeader(e.getKey(), e.getValue());
			}
			httpResponse.setHeader(header);

			return httpResponse;
		} catch (HttpDownloaderException e) {
			throw new HttpdownloaderServiceException(e);
		}
	}
}
