package de.benjaminborbe.tools.http;

import java.net.URL;

public interface HttpDownloader {

	/**
	 * timeOut in milliseconds
	 */
	HttpDownloadResult downloadUrl(URL url, int timeOut) throws HttpDownloaderException;

	/**
	 * timeOut in milliseconds
	 */
	HttpDownloadResult downloadUrl(URL url, int timeOut, String username, String password) throws HttpDownloaderException;

	/**
	 * timeOut in milliseconds
	 */
	HttpDownloadResult downloadUrlUnsecure(URL url, int timeOut) throws HttpDownloaderException;

	/**
	 * timeOut in milliseconds
	 */
	HttpDownloadResult downloadUrlUnsecure(URL url, int timeOut, String username, String password) throws HttpDownloaderException;
}
