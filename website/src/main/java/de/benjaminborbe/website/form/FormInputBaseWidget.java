package de.benjaminborbe.website.form;

import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.website.util.CompositeWidget;
import de.benjaminborbe.website.util.ListWidget;
import de.benjaminborbe.website.util.SingleTagWidget;
import de.benjaminborbe.website.util.TagWidget;
import de.benjaminborbe.website.widget.BrWidget;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class FormInputBaseWidget extends CompositeWidget implements FormInputWidget<FormInputBaseWidget> {

	private String label;

	private String defaultValue;

	private String value;

	private boolean br = true;

	private final String type;

	private final String name;

	private String id;

	private String placeholder;

	public FormInputBaseWidget(final String type, final String name) {
		this.type = type;
		this.name = name;
	}

	public FormInputBaseWidget setBr(final boolean br) {
		this.br = br;
		return this;
	}

	@Override
	public FormInputBaseWidget addLabel(final String label) {
		this.label = label;
		return this;
	}

	@Override
	public FormInputBaseWidget addDefaultValue(final Object defaultValue) {
		this.defaultValue = defaultValue != null ? String.valueOf(defaultValue) : null;
		return this;
	}

	@Override
	public FormInputBaseWidget addPlaceholder(final String placeholder) {
		this.placeholder = placeholder;
		return this;
	}

	@Override
	public FormInputBaseWidget addId(final String id) {
		this.id = id;
		return this;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public FormInputBaseWidget addValue(final Object value) {
		this.value = value != null ? String.valueOf(value) : null;
		return this;
	}

	@Override
	protected Widget createWidget(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws Exception {
		final ListWidget widgets = new ListWidget();
		if (label != null) {
			widgets.add(new TagWidget("label", label).addAttribute("for", getName()));
		}
		widgets.add(createInputWidget(request));
		if (br) {
			widgets.add(new BrWidget());
		}
		return widgets;
	}

	protected SingleTagWidget createInputWidget(final HttpServletRequest request) {
		final String value = this.value != null ? this.value : (request.getParameter(getName()) != null ? request.getParameter(getName()) : defaultValue);
		final SingleTagWidget input = new SingleTagWidget("input");
		if (value != null) {
			input.addAttribute("value", value);
		}
		if (name != null) {
			input.addAttribute("name", name);
		}
		if (type != null) {
			input.addAttribute("type", type);
		}
		if (placeholder != null) {
			input.addAttribute("placeholder", placeholder);
		}
		if (id != null) {
			input.addAttribute("id", id);
		}
		return input;
	}

}
