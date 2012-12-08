package de.benjaminborbe.website.form;

import javax.servlet.http.HttpServletRequest;

import de.benjaminborbe.website.util.SingleTagWidget;

public class FormInputPasswordWidget extends FormInputBaseWidget {

	public FormInputPasswordWidget(final String name) {
		super("password", name);
	}

	@Override
	protected SingleTagWidget createInputWidget(final HttpServletRequest request) {
		final SingleTagWidget widget = super.createInputWidget(request);
		widget.addAttribute("autocomplete", "off");
		return widget;
	}
}
