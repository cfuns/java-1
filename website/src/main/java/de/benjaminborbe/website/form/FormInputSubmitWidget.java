package de.benjaminborbe.website.form;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.website.util.CompositeWidget;
import de.benjaminborbe.website.util.SingleTagWidget;

public class FormInputSubmitWidget extends CompositeWidget implements FormElementWidget {

	private final String buttonName;

	public FormInputSubmitWidget(final String buttonName) {
		this.buttonName = buttonName;
	}

	@Override
	protected Widget createWidget(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws Exception {
		final SingleTagWidget input = new SingleTagWidget("input");
		input.addAttribute("type", "submit");
		input.addAttribute("value", buttonName);
		return input;
	}
}
