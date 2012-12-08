package de.benjaminborbe.website.form;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.website.util.CompositeWidget;
import de.benjaminborbe.website.util.SingleTagWidget;

public class FormInputHiddenWidget extends CompositeWidget implements FormElementWidget, HasValue<FormInputHiddenWidget>, HasDefaultValue<FormInputHiddenWidget> {

	private String defaultValue;

	private String value;

	private final String name;

	public FormInputHiddenWidget(final String name) {
		this.name = name;
	}

	@Override
	public FormInputHiddenWidget addValue(final Object value) {
		this.value = value != null ? String.valueOf(value) : null;
		return this;
	}

	@Override
	public String getValue() {
		return value;
	}

	public String getName() {
		return name;
	}

	@Override
	public FormInputHiddenWidget addDefaultValue(final Object defaultValue) {
		this.defaultValue = defaultValue != null ? String.valueOf(defaultValue) : null;
		return this;
	}

	@Override
	public String getDefaultValue() {
		return defaultValue;
	}

	@Override
	protected Widget createWidget(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws Exception {
		final SingleTagWidget input = new SingleTagWidget("input");
		input.addAttribute("type", "hidden");
		final String value = this.value != null ? this.value : (request.getParameter(getName()) != null ? request.getParameter(getName()) : defaultValue);
		input.addAttribute("value", value);
		input.addAttribute("name", name);
		return input;
	}
}
