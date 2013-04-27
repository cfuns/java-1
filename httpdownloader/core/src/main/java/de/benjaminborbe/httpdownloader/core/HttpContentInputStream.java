package de.benjaminborbe.httpdownloader.core;

import de.benjaminborbe.httpdownloader.api.HttpContent;
import de.benjaminborbe.tools.stream.ChannelTools;
import de.benjaminborbe.tools.stream.StreamUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class HttpContentInputStream implements HttpContent {

	private final InputStream inputStream;

	public HttpContentInputStream(final InputStream inputStream) {
		this.inputStream = inputStream;
	}

	@Override
	public byte[] getContent() {
		try {
			final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			final StreamUtil streamUtil = new StreamUtil(new ChannelTools());
			streamUtil.copy(inputStream, byteArrayOutputStream);
			return byteArrayOutputStream.toByteArray();
		} catch (IOException e) {
			return null;
		}
	}

	@Override
	public InputStream getContentStream() {
		return inputStream;
	}
}
