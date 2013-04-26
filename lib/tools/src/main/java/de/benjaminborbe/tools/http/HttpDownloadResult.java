package de.benjaminborbe.tools.http;

import de.benjaminborbe.tools.util.Encoding;

import java.net.URL;
import java.util.List;
import java.util.Map;

public interface HttpDownloadResult {

	URL getUrl();

	long getDuration();

	int getResponseCode();

	byte[] getContent();

	Encoding getContentEncoding();

	String getContentType();

	Map<String, List<String>> getHeaders();

}
