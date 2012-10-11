package de.benjaminborbe.website.util;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;

public class ExceptionWidget implements Widget {

	private final Throwable exception;

	public ExceptionWidget(final Throwable exception) {
		this.exception = exception;
	}

	@Override
	public void render(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException {
		final PrintWriter out = response.getWriter();
		out.println("<h1>Exception occurred: " + exception.getClass().getName() + "</h1>");
		out.println("Message: " + exception.getMessage());
		out.println("Stack: <pre>");
		exception.printStackTrace(out);
		out.println("</pre>");
	}

}
