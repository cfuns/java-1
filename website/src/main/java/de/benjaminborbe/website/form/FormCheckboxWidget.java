package de.benjaminborbe.website.form;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.website.util.SingleTagWidget;

public class FormCheckboxWidget implements FormElementWidget, HasName<FormCheckboxWidget>, HasValue<FormCheckboxWidget>, HasDefaultValue<FormCheckboxWidget>,
		HasOnClick<FormCheckboxWidget> {

	private String name;

	private String onclick;

	private String value;

	private String defaultValue;

	public FormCheckboxWidget(final String name) {
		this.name = name;
	}

	@Override
	public void render(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException {
		final String value = this.value != null ? this.value : (request.getParameter(getName()) != null ? request.getParameter(getName()) : defaultValue);
		final boolean checked = "true".equals(value);

		final SingleTagWidget widget = new SingleTagWidget("input");
		widget.addAttribute("type", "checkbox");
		if (name != null) {
			widget.addAttribute("name", name);
		}
		if (onclick != null) {
			widget.addAttribute("onclick", onclick);
		}
		if (value != null) {
			widget.addAttribute("value", "true");
		}
		if (checked) {
			widget.addAttribute("checked", "checked");
		}
		widget.render(request, response, context);
	}

	@Override
	public FormCheckboxWidget addName(final String name) {
		this.name = name;
		return this;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public FormCheckboxWidget addOnClick(final String onclick) {
		this.onclick = onclick;
		return this;
	}

	@Override
	public String getOnClick() {
		return onclick;
	}

	@Override
	public FormCheckboxWidget addValue(final Object value) {
		this.value = value != null ? String.valueOf(value) : null;
		return this;
	}

	@Override
	public String getValue() {
		return value;
	}

	@Override
	public FormCheckboxWidget addDefaultValue(final Object value) {
		this.defaultValue = value != null ? String.valueOf(value) : null;
		return this;
	}

	@Override
	public String getDefaultValue() {
		return defaultValue;
	}
}
