package de.benjaminborbe.tools.http;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import de.benjaminborbe.tools.util.Encoding;

public class HttpDownloadResult implements Serializable {

	private static final long serialVersionUID = -3165056995643380843L;

	// wie lang der download gedauert hat
	private final long duration;

	// inhalt des downloads
	private final byte[] content;

	// encoding des contents
	private final Encoding contentEncoding;

	private final String contentType;

	private final Map<String, List<String>> headers;

	public HttpDownloadResult(final long duration, final byte[] content, final String contentType, final Encoding contentEncoding, final Map<String, List<String>> headers) {
		this.duration = duration;
		this.contentType = contentType;
		this.content = Arrays.copyOf(content, content.length);
		this.contentEncoding = contentEncoding;
		this.headers = headers;
	}

	public long getDuration() {
		return duration;
	}

	public byte[] getContent() {
		return content;
	}

	public Encoding getContentEncoding() {
		return contentEncoding;
	}

	public String getContentType() {
		return contentType;
	}

	public Map<String, List<String>> getHeaders() {
		return headers;
	}

}
