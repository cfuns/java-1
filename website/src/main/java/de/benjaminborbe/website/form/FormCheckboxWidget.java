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
		HasOnClick<FormCheckboxWidget> {

	private String name;

	private String onclick;

	private String value = "true";

	private String label;

	private Boolean checked;

	private boolean checkedDefault = false;

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
		if (value == null) {
			throw new NullPointerException("value can't be null");
		}
		this.value = String.valueOf(value);
		return this;
	}

	@Override
	public String getValue() {
		return value;
	}

	@Override
	public FormCheckboxWidget addLabel(final String label) {
		this.label = label;
		return this;
	}

	public FormCheckboxWidget setCheck(final Boolean checked) {
		this.checked = checked;
		return this;
	}

	@Override
	public String getLabel() {
		return label;
	}

	@Override
	protected Widget createWidget(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws Exception {
		final boolean checked = isChecked(request);
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
		inputTag.addAttribute("value", value);
		if (checked) {
			inputTag.addAttribute("checked", "checked");
		}
		widgets.add(inputTag);
		widgets.add(new BrWidget());
		return widgets;
	}

	private boolean isChecked(final HttpServletRequest request) {
		if (checked != null) {
			return checked;
		}
		if (request.getParameter(getName()) == null) {
			return checkedDefault;
		}
		for (final String value : request.getParameterValues(getName())) {
			if (this.value.equals(value)) {
				return true;
			}
		}
		return false;
	}

	public FormElementWidget setCheckedDefault(final boolean checkedDefault) {
		this.checkedDefault = checkedDefault;
		return this;
	}
}
