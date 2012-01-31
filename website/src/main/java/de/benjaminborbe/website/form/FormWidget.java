package de.benjaminborbe.website.form;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.tools.html.Target;

public class FormWidget implements Widget {

	private final String action;

	private final List<FormInputWidget> formInputWidgets = new ArrayList<FormInputWidget>();

	private String method;

	private Target target;

	public FormWidget(final String action) {
		this.action = action;
	}

	public FormWidget addFormInputWidget(final FormInputWidget formInputWidget) {
		formInputWidgets.add(formInputWidget);
		return this;
	}

	public FormWidget addMethod(final String method) {
		this.method = method;
		return this;
	}

	@Override
	public void render(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException {
		final PrintWriter out = response.getWriter();
		out.print("<form action=\"" + action + "\"");
		if (method != null) {
			out.print(" method=\"" + method + "\"");
		}
		if (target != null) {
			out.print(" target=\"" + target + "\"");
		}
		out.print(">");
		out.println("<fieldset>");
		for (final FormInputWidget formInputWidget : formInputWidgets) {
			formInputWidget.render(request, response, context);
		}
		out.println("</fieldset>");
		out.println("</form>");
	}

	public FormWidget addTarget(final Target target) {
		this.target = target;
		return this;
	}

}
