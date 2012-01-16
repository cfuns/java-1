package de.benjaminborbe.website.util;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.benjaminborbe.html.api.HttpContext;

public class FormInputBaseWidget implements FormInputWidget {

	private final String name;

	private final String label;

	private final String defaultValue;

	private final String type;

	public FormInputBaseWidget(final String type, final String name, final String label, final String defaultValue) {
		this.type = type;
		this.name = name;
		this.label = label;
		this.defaultValue = defaultValue;
	}

	@Override
	public void render(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException {
		final String value = request.getParameter(name) != null ? request.getParameter(name) : defaultValue;
		final PrintWriter out = response.getWriter();
		out.println("<label for=\"" + name + "\">" + label + "</label>");
		out.println("<input type=\"" + type + "\" name=\"" + name + "\" value=\"" + value + "\">");
	}
}
