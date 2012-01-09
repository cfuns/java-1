package de.benjaminborbe.tools.http;

import java.io.Serializable;

import de.benjaminborbe.tools.util.Encoding;

public class HttpDownloadResult implements Serializable {

	private static final long serialVersionUID = -3165056995643380843L;

	// wie lang der download gedauert hat
	private final long duration;

	// inhalt des downloads
	private final byte[] content;

	// encoding des contents
	private final Encoding contentEncoding;

	public HttpDownloadResult(final long duration, final byte[] content, final Encoding contentEncoding) {
		this.duration = duration;
		this.content = content;
		this.contentEncoding = contentEncoding;
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

}
