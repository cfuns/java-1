package de.benjaminborbe.httpdownloader.core.util;

import de.benjaminborbe.tools.http.HttpDownloadResult;
import de.benjaminborbe.tools.http.HttpDownloaderException;

import java.net.URL;

public interface HttpdownloaderGet {

	HttpDownloadResult getUrl(URL url, int timeOut) throws HttpDownloaderException;
}
