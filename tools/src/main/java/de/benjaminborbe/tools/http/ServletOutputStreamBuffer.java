package de.benjaminborbe.tools.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.servlet.ServletOutputStream;

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
