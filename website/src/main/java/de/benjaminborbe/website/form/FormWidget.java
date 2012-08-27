package de.benjaminborbe.website.form;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.tools.html.Target;

public class FormWidget implements Widget {

	private final String action;

	private final List<FormElementWidget> formInputWidgets = new ArrayList<FormElementWidget>();

	private FormMethod method;

	private Target target;

	private FormEncType encType;

	public FormWidget(final String action) {
		this.action = action;
	}

	public FormWidget addFormInputWidget(final FormElementWidget formInputWidget) {
		if (formInputWidget == null) {
			throw new NullPointerException("can't add null FormElementWidget");
		}
		formInputWidgets.add(formInputWidget);
		return this;
	}

	public FormWidget addMethod(final FormMethod method) {
		this.method = method;
		return this;
	}

	@Override
	public void render(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException, PermissionDeniedException {
		final PrintWriter out = response.getWriter();
		out.print("<form action=\"" + action + "\"");
		if (method != null) {
			out.print(" method=\"" + method + "\"");
		}
		if (target != null) {
			out.print(" target=\"" + target + "\"");
		}
		if (encType != null) {
			out.print(" enctype=\"" + encType + "\"");
		}
		out.println(">");
		out.println("<fieldset>");
		for (final FormElementWidget formInputWidget : formInputWidgets) {
			formInputWidget.render(request, response, context);
		}
		out.println("</fieldset>");
		out.println("</form>");
	}

	public FormWidget addTarget(final Target target) {
		this.target = target;
		return this;
	}

	public FormWidget addEncType(final FormEncType encType) {
		this.encType = encType;
		return this;
	}

}
