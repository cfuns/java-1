package de.benjaminborbe.tools.http;

import java.net.URL;
import java.util.Map;

public interface HttpDownloader {

	/**
	 * timeOut in milliseconds
	 */
	HttpDownloadResult getUrl(URL url, int timeOut) throws HttpDownloaderException;

	/**
	 * timeOut in milliseconds
	 */
	HttpDownloadResult getUrl(URL url, int timeOut, String username, String password) throws HttpDownloaderException;

	/**
	 * timeOut in milliseconds
	 */
	HttpDownloadResult getUrlUnsecure(URL url, int timeOut) throws HttpDownloaderException;

	/**
	 * timeOut in milliseconds
	 */
	HttpDownloadResult getUrlUnsecure(URL url, int timeOut, String username, String password) throws HttpDownloaderException;

	/**
	 * timeOut in milliseconds
	 */
	HttpDownloadResult postUrl(URL url, Map<String, String> data, int timeOut) throws HttpDownloaderException;

	HttpDownloadResult postUrl(final URL url, final Map<String, String> data, final Map<String, String> cookies, int timeOut) throws HttpDownloaderException;

}
