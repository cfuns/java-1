package de.benjaminborbe.website.form;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.benjaminborbe.html.api.HttpContext;

public class FormInputBaseWidget implements FormInputWidget {

	private String name;

	private String label;

	private String defaultValue;

	private String type;

	private String placeholder;

	private String id;

	public FormInputBaseWidget(final String type, final String name) {
		this.type = type;
		this.name = name;
	}

	@Override
	public void render(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException {
		final String value = request.getParameter(name) != null ? request.getParameter(name) : defaultValue;
		final PrintWriter out = response.getWriter();
		if (label != null) {
			out.println("<label for=\"" + name + "\">" + label + "</label>");
		}
		out.print("<input");
		if (id != null) {
			out.print(" id=\"" + id + "\"");
		}
		if (type != null) {
			out.print(" type=\"" + type + "\"");
		}
		if (name != null) {
			out.print(" name=\"" + name + "\"");
		}
		if (value != null) {
			out.print(" value=\"" + value + "\"");
		}
		if (placeholder != null) {
			out.print(" placeholder=\"" + placeholder + "\"");
		}
		out.println("/>");
		out.println("<br/>");
	}

	public FormInputBaseWidget addName(final String name) {
		this.name = name;
		return this;
	}

	public FormInputBaseWidget addLabel(final String label) {
		this.label = label;
		return this;
	}

	public FormInputBaseWidget addDefaultValue(final String defaultValue) {
		this.defaultValue = defaultValue;
		return this;
	}

	public FormInputBaseWidget addType(final String type) {
		this.type = type;
		return this;
	}

	public FormInputBaseWidget addPlaceholder(final String placeholder) {
		this.placeholder = placeholder;
		return this;
	}

	public FormInputBaseWidget addId(final String id) {
		this.id = id;
		return this;
	}

}
