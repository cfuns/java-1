package de.benjaminborbe.tools.http;

import java.io.UnsupportedEncodingException;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class HttpDownloadUtil {

	@Inject
	public HttpDownloadUtil() {
	}

	public String getContent(final HttpDownloadResult result) throws UnsupportedEncodingException {
		if (result.getContent() == null) {
			return null;
		}
		else if (result.getContentEncoding() != null && result.getContentEncoding().getEncoding() != null) {
			return new String(result.getContent(), result.getContentEncoding().getEncoding());
		}
		else {
			return new String(result.getContent());
		}
	}
}
