package de.benjaminborbe.tools.http;

import java.util.Arrays;

public class HttpCacheEntry {

	private final String contentType;

	private final String writerContent;

	private final byte[] streamContent;

	public HttpCacheEntry(final String contentType, final String writerContent, final byte[] streamContent) {
		this.contentType = contentType;
		this.writerContent = writerContent;
		this.streamContent = Arrays.copyOf(streamContent, streamContent.length);
	}

	public String getContentType() {
		return contentType;
	}

	public String getWriterContent() {
		return writerContent;
	}

	public byte[] getStreamContent() {
		return streamContent;
	}

}
