package de.benjaminborbe.website.form;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.website.util.CompositeWidget;
import de.benjaminborbe.website.util.ListWidget;
import de.benjaminborbe.website.util.SingleTagWidget;
import de.benjaminborbe.website.util.TagWidget;
import de.benjaminborbe.website.widget.BrWidget;

public class FormCheckboxWidget extends CompositeWidget implements FormElementWidget, HasLabel<FormCheckboxWidget>, HasName<FormCheckboxWidget>, HasValue<FormCheckboxWidget>,
		HasDefaultValue<FormCheckboxWidget>, HasOnClick<FormCheckboxWidget> {

	private String name;

	private String onclick;

	private String value;

	private String defaultValue;

	private String label;

	public FormCheckboxWidget(final String name) {
		this.name = name;
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

	@Override
	public FormCheckboxWidget addLabel(final String label) {
		this.label = label;
		return this;
	}

	@Override
	public String getLabel() {
		return label;
	}

	@Override
	protected Widget createWidget(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws Exception {
		final String value = this.value != null ? this.value : (request.getParameter(getName()) != null ? request.getParameter(getName()) : defaultValue);
		final boolean checked = "true".equals(value);
		final ListWidget widgets = new ListWidget();
		if (label != null) {
			widgets.add(new TagWidget("label", label).addAttribute("for", getName()));
		}
		final SingleTagWidget inputTag = new SingleTagWidget("input");
		inputTag.addAttribute("type", "checkbox");
		if (name != null) {
			inputTag.addAttribute("name", name);
		}
		if (onclick != null) {
			inputTag.addAttribute("onclick", onclick);
		}
		inputTag.addAttribute("value", "true");
		if (checked) {
			inputTag.addAttribute("checked", "checked");
		}
		widgets.add(inputTag);
		widgets.add(new BrWidget());
		return widgets;
	}
}
