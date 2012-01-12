package de.benjaminborbe.tools.http;

import java.io.IOException;
import java.net.URL;

public interface HttpDownloader {

	HttpDownloadResult downloadUrl(URL url, int timeOut) throws IOException;

	HttpDownloadResult downloadUrl(URL url, int timeOut, String username, String password) throws IOException;

	HttpDownloadResult downloadUrlUnsecure(URL url, int timeOut) throws IOException;

	HttpDownloadResult downloadUrlUnsecure(URL url, int timeOut, String username, String password) throws IOException;
}
