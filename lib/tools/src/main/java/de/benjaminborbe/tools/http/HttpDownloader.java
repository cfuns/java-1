package de.benjaminborbe.tools.http;

import java.net.URL;
import java.util.Map;

public interface HttpDownloader {

	String USERAGENT = "Yet Another Bot";

	HttpDownloadResult getUrl(URL url, int timeOut) throws HttpDownloaderException;

	HttpDownloadResult getUrl(URL url, int timeOut, Map<String, String> cookies) throws HttpDownloaderException;

	HttpDownloadResult getUrl(URL url, int timeOut, String username, String password) throws HttpDownloaderException;

	HttpDownloadResult getUrl(URL url, int timeOut, String username, String password, Map<String, String> cookies) throws HttpDownloaderException;

	HttpDownloadResult getUrlUnsecure(URL url, int timeOut) throws HttpDownloaderException;

	HttpDownloadResult getUrlUnsecure(URL url, int timeOut, String username, String password) throws HttpDownloaderException;

	HttpDownloadResult postUrl(URL url, Map<String, String> data, int timeOut) throws HttpDownloaderException;

	HttpDownloadResult postUrl(final URL url, final Map<String, String> data, final Map<String, String> cookies, int timeOut) throws HttpDownloaderException;

}
