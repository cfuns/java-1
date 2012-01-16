package de.benjaminborbe.website.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;

public class FormWidget implements Widget {

	private final String action;

	private final List<FormInputWidget> formInputWidgets;

	public FormWidget(final String action, final List<FormInputWidget> formInputWidgets) {
		this.action = action;
		this.formInputWidgets = formInputWidgets;
	}

	@Override
	public void render(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException {
		final PrintWriter out = response.getWriter();
		out.println("<form action=\"" + action + "\" method=\"POST\">");
		out.println("<fieldset>");
		for (final FormInputWidget formInputWidget : formInputWidgets) {
			formInputWidget.render(request, response, context);
		}
		out.println("</fieldset>");
		out.println("</form>");
	}

}
