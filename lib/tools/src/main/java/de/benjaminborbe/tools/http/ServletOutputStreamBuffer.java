package de.benjaminborbe.tools.http;

import javax.servlet.ServletOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ServletOutputStreamBuffer extends ServletOutputStream {

	private final ByteArrayOutputStream o = new ByteArrayOutputStream();

	@Override
	public void write(final int b) throws IOException {
		o.write(b);
	}

	public byte[] toByteArray() {
		return o.toByteArray();
	}
}
