package de.benjaminborbe.httpdownloader.api;

public interface HttpdownloaderService {

	String USERAGENT = "Yet Another Bot";

	HttpResponse fetch(HttpRequest httpRequest) throws HttpdownloaderServiceException;

}
