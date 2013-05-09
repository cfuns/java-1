package de.benjaminborbe.httpdownloader.core.util;

import de.benjaminborbe.httpdownloader.api.HttpRequest;

public interface HttpdownloaderAction {

	HttpDownloadResult fetch(final HttpRequest httpRequest) throws HttpDownloaderException;
}
