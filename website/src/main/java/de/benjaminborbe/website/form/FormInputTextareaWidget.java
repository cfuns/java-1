package de.benjaminborbe.website.form;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.website.util.CompositeWidget;
import de.benjaminborbe.website.util.ListWidget;
import de.benjaminborbe.website.util.StringWidget;
import de.benjaminborbe.website.util.TagWidget;
import de.benjaminborbe.website.widget.BrWidget;

public class FormInputTextareaWidget extends CompositeWidget implements FormInputWidget<FormInputTextareaWidget> {

	private String label;

	private String defaultValue;

	private String value;

	private final String name;

	private String id;

	public FormInputTextareaWidget(final String name) {
		this.name = name;
	}

	@Override
	public FormInputTextareaWidget addLabel(final String label) {
		this.label = label;
		return this;
	}

	@Override
	public FormInputTextareaWidget addDefaultValue(final Object defaultValue) {
		this.defaultValue = defaultValue != null ? String.valueOf(defaultValue) : null;
		return this;
	}

	@Override
	public FormInputTextareaWidget addPlaceholder(final String placeholder) {
		return this;
	}

	@Override
	public FormInputTextareaWidget addId(final String id) {
		this.id = id;
		return this;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public FormInputTextareaWidget addValue(final Object value) {
		this.value = value != null ? String.valueOf(value) : null;
		return this;
	}

	@Override
	protected Widget createWidget(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws Exception {
		final ListWidget widgets = new ListWidget();

		if (label != null) {
			widgets.add(new TagWidget("label", label).addAttribute("for", getName()));
		}

		final TagWidget textarea = new TagWidget("textarea");
		textarea.addAttribute("name", name);
		final String value = this.value != null ? this.value : (request.getParameter(getName()) != null ? request.getParameter(getName()) : defaultValue);
		if (value != null) {
			textarea.addContent(new StringWidget(value));
		}
		if (id != null) {
			textarea.addAttribute("id", id);
		}
		widgets.add(textarea);
		widgets.add(new BrWidget());
		return widgets;
	}
}
