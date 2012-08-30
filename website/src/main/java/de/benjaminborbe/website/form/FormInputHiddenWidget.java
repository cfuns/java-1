package de.benjaminborbe.website.form;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.website.util.SingleTagWidget;

public class FormInputHiddenWidget extends SingleTagWidget implements FormElementWidget {

	private static final String VALUE = "value";

	private static final String TYPE = "type";

	private static final String NAME = "name";

	public FormInputHiddenWidget(final String valueName) {
		super("input");
		if (valueName == null) {
			throw new NullPointerException("parameter valueName is null");
		}
		addAttribute(TYPE, "hidden");
		addAttribute(NAME, valueName);
	}

	public String getValue() {
		return getAttribute(VALUE);
	}

	public String getName() {
		return getAttribute(NAME);
	}

	@Override
	public void render(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException {
		final String value = request.getParameter(getName());
		addAttribute(VALUE, value != null ? value : "");
		super.render(request, response, context);
	}
}
