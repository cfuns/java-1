package de.benjaminborbe.httpdownloader.mock;

import de.benjaminborbe.httpdownloader.api.HttpRequest;
import de.benjaminborbe.httpdownloader.api.HttpResponse;
import de.benjaminborbe.httpdownloader.api.HttpdownloaderService;
import de.benjaminborbe.httpdownloader.api.HttpdownloaderServiceException;

import javax.inject.Singleton;

@Singleton
public class HttpdownloaderServiceMock implements HttpdownloaderService {

	private HttpResponse httpResponse;

	public HttpdownloaderServiceMock() {
	}

	@Override
	public HttpResponse fetch(final HttpRequest httpRequest) throws HttpdownloaderServiceException {
		return httpResponse;
	}

	public void setHttpResponse(final HttpResponse httpResponse) {
		this.httpResponse = httpResponse;
	}

}
