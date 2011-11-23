package de.benjaminborbe.index.util;

import java.io.IOException;
import java.net.URL;

public interface HttpDownloader {

	HttpDownloadResult downloadUrl(URL url) throws IOException;

	HttpDownloadResult downloadUrlUnsecure(URL url) throws IOException;
}
