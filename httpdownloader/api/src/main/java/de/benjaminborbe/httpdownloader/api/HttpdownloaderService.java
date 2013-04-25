package de.benjaminborbe.httpdownloader.api;

public interface HttpdownloaderService {

	HttpResponse get(HttpRequest httpRequest) throws HttpdownloaderServiceException;
}
