package de.benjaminborbe.httpdownloader.api;

public interface HttpdownloaderService {

	HttpResponse getSecure(HttpRequest httpRequest) throws HttpdownloaderServiceException;

	HttpResponse getUnsecure(HttpRequest httpRequest) throws HttpdownloaderServiceException;
}
