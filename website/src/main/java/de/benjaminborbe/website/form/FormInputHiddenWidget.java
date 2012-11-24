package de.benjaminborbe.website.form;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.website.util.SingleTagWidget;

public class FormInputHiddenWidget extends SingleTagWidget implements FormElementWidget, HasValue<FormInputHiddenWidget>, HasDefaultValue<FormInputHiddenWidget> {

	private static final String TYPE = "type";

	private static final String NAME = "name";

	private String defaultValue;

	private String value;

	public FormInputHiddenWidget(final String valueName) {
		super("input");
		if (valueName == null) {
			throw new NullPointerException("parameter valueName is null");
		}
		addAttribute(TYPE, "hidden");
		addAttribute(NAME, valueName);
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
		return getAttribute(NAME);
	}

	@Override
	public void render(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException {
		final String value = this.value != null ? this.value : (request.getParameter(getName()) != null ? request.getParameter(getName()) : defaultValue);
		addAttribute("value", value);
		super.render(request, response, context);
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
}
