package de.benjaminborbe.httpdownloader.api;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class HttpContentByteArray implements HttpContent {

	private final byte[] content;

	public HttpContentByteArray(final byte[] content) {
		this.content = content;
	}

	@Override
	public byte[] getContent() {
		return content;
	}

	@Override
	public InputStream getContentStream() {
		return new ByteArrayInputStream(content);
	}
}
