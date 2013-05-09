package de.benjaminborbe.httpdownloader.core.util;

import de.benjaminborbe.httpdownloader.api.HttpHeader;

import java.net.URL;
import java.util.Map;

public interface HttpDownloader {

	String USERAGENT = "Yet Another Bot";

	HttpDownloadResult getUrlSecure(URL url, Integer timeOut, String username, String password, final HttpHeader header) throws HttpDownloaderException;

	HttpDownloadResult getUrlUnsecure(URL url, Integer timeOut, String username, String password, final HttpHeader header) throws HttpDownloaderException;

	HttpDownloadResult postUrlSecure(final URL url, Integer timeOut, final Map<String, String> data, final HttpHeader header) throws HttpDownloaderException;

	HttpDownloadResult postUrlUnsecure(
		final URL url,
		Integer timeOut,
		final Map<String, String> data,
		final HttpHeader header
	) throws HttpDownloaderException;

}
