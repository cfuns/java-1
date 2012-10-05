package de.benjaminborbe.website.form;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.website.util.StringWidget;
import de.benjaminborbe.website.util.TagWidget;

public class FormInputTextareaWidget extends TagWidget implements FormInputWidget {

	private String label;

	private String defaultValue;

	public FormInputTextareaWidget(final String name) {
		super("textarea");
		addAttribute("name", name);
	}

	@Override
	public FormInputTextareaWidget addLabel(final String label) {
		this.label = label;
		return this;
	}

	@Override
	public FormInputTextareaWidget addDefaultValue(final String defaultValue) {
		this.defaultValue = defaultValue;
		return this;
	}

	@Override
	public FormInputTextareaWidget addPlaceholder(final String placeholder) {
		return this;
	}

	@Override
	public FormInputTextareaWidget addId(final String id) {
		addAttribute("id", id);
		return this;
	}

	@Override
	public String getName() {
		return getAttribute("name");
	}

	@Override
	public void render(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException {
		final String value = request.getParameter(getName()) != null ? request.getParameter(getName()) : defaultValue;
		if (value != null) {
			addContent(new StringWidget(value));
		}
		final PrintWriter out = response.getWriter();
		if (label != null) {
			out.println("<label for=\"" + getName() + "\">" + label + "</label>");
		}
		super.render(request, response, context);
		out.print("<br/>");
	}
}
