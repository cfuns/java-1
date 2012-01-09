package de.benjaminborbe.tools.http;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

public class HttpServletResponseBuffer extends HttpServletResponseAdapter {

	private final StringWriter writer = new StringWriter();

	public HttpServletResponseBuffer(final HttpServletResponse response) {
		super(response);
	}

	@Override
	public ServletOutputStream getOutputStream() throws IOException {
		throw new IllegalArgumentException("can't be used");
	}

	@Override
	public PrintWriter getWriter() throws IOException {
		return new PrintWriter(writer);
	}

	public StringWriter getStringWriter() {
		return writer;
	}
}
