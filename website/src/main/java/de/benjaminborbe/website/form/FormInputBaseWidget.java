package de.benjaminborbe.website.form;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.benjaminborbe.html.api.HttpContext;

public class FormInputBaseWidget implements FormElementWidget, FormInputWidget {

	private final String name;

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
		final String value = request.getParameter(getName()) != null ? request.getParameter(getName()) : defaultValue;
		final PrintWriter out = response.getWriter();
		if (label != null) {
			out.println("<label for=\"" + getName() + "\">" + label + "</label>");
		}
		out.print("<input");
		if (id != null) {
			out.print(" id=\"" + id + "\"");
		}
		if (type != null) {
			out.print(" type=\"" + type + "\"");
		}
		if (getName() != null) {
			out.print(" name=\"" + getName() + "\"");
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

	@Override
	public FormInputBaseWidget addLabel(final String label) {
		this.label = label;
		return this;
	}

	@Override
	public FormInputBaseWidget addDefaultValue(final String defaultValue) {
		this.defaultValue = defaultValue;
		return this;
	}

	@Override
	public FormInputBaseWidget addType(final String type) {
		this.type = type;
		return this;
	}

	@Override
	public FormInputBaseWidget addPlaceholder(final String placeholder) {
		this.placeholder = placeholder;
		return this;
	}

	@Override
	public FormInputBaseWidget addId(final String id) {
		this.id = id;
		return this;
	}

	public String getName() {
		return name;
	}

}
