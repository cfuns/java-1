package de.benjaminborbe.tools.stream;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class InputStreamCopy extends InputStream {

	private final InputStream inputStream;

	private final OutputStream outputStream;

	public InputStreamCopy(final InputStream inputStream, final OutputStream outputStream) {
		this.inputStream = inputStream;
		this.outputStream = outputStream;
	}

	@Override
	public int read() throws IOException {
		final int result = inputStream.read();
		if (result != -1) {
			outputStream.write(result);
		}
		return result;
	}
}
