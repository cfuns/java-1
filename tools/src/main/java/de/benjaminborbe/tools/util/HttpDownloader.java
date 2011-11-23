package de.benjaminborbe.tools.util;

import java.io.IOException;
import java.net.URL;

public interface HttpDownloader {

	HttpDownloadResult downloadUrl(URL url, int timeOut) throws IOException;

	HttpDownloadResult downloadUrlUnsecure(URL url, int timeOut) throws IOException;
}
