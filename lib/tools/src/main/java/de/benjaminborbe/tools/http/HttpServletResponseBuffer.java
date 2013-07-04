package de.benjaminborbe.tools.http;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

public class HttpServletResponseBuffer extends HttpServletResponseAdapter implements HttpServletResponse {

	private final StringWriter writer = new StringWriter();

	private final ServletOutputStreamBuffer outputStream = new ServletOutputStreamBuffer();

	public HttpServletResponseBuffer(final HttpServletResponse response) {
		super(response);
	}

	@Override
	public ServletOutputStream getOutputStream() throws IOException {
		return outputStream;
	}

	@Override
	public PrintWriter getWriter() throws IOException {
		return new PrintWriter(writer);
	}

	public String getWriterContent() {
		return writer.toString();
	}

	public byte[] getOutputStreamContent() {
		return outputStream.toByteArray();
	}

	public StringWriter getStringWriter() {
		return writer;
	}
}
