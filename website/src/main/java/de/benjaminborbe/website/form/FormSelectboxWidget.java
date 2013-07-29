package de.benjaminborbe.website.form;

import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.website.util.CompositeWidget;
import de.benjaminborbe.website.util.ListWidget;
import de.benjaminborbe.website.util.TagWidget;
import de.benjaminborbe.website.widget.BrWidget;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

public class FormSelectboxWidget extends CompositeWidget implements FormElementWidget, HasPaceholder<FormSelectboxWidget>, HasId<FormSelectboxWidget>,
	HasOption<FormSelectboxWidget>, HasLabel<FormSelectboxWidget>, HasDefaultValue<FormSelectboxWidget>, HasName<FormSelectboxWidget>, HasValue<FormSelectboxWidget> {

	private final class Option {

		private final String value;

		private final String name;

		public Option(final String value, final String name) {
			this.value = value;
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public String getValue() {
			return value;
		}
	}

	private final List<Option> options = new ArrayList<Option>();

	private String name;

	private String label;

	private String id;

	private String defaultValue;

	private String placeholder;

	private String value;

	public FormSelectboxWidget(final String name) {
		this.name = name;
	}

	@Override
	public FormSelectboxWidget addName(final String name) {
		this.name = name;
		return this;
	}

	@Override
	public FormSelectboxWidget addLabel(final String label) {
		this.label = label;
		return this;
	}

	@Override
	public FormSelectboxWidget addId(final String id) {
		this.id = id;
		return this;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getLabel() {
		return label;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public FormSelectboxWidget addOption(final String value, final String name) {
		options.add(new Option(value, name));
		return this;
	}

	@Override
	public FormSelectboxWidget addDefaultValue(final Object defaultValue) {
		this.defaultValue = defaultValue != null ? String.valueOf(defaultValue) : null;
		return this;
	}

	@Override
	public String getDefaultValue() {
		return defaultValue;
	}

	@Override
	public FormSelectboxWidget addPlaceholder(final String placeholder) {
		this.placeholder = placeholder;
		return this;
	}

	@Override
	public String getPlaceholder() {
		return placeholder;
	}

	@Override
	public FormSelectboxWidget addValue(final Object value) {
		this.value = value != null ? String.valueOf(value) : null;
		return this;
	}

	@Override
	public String getValue() {
		return value;
	}

	@Override
	protected Widget createWidget(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws Exception {
		final String value = this.value != null ? this.value : (request.getParameter(getName()) != null ? request.getParameter(getName()) : defaultValue);
		final ListWidget widgets = new ListWidget();
		if (label != null) {
			widgets.add(new TagWidget("label", label).addAttribute("for", getName()));
		}
		final TagWidget select = new TagWidget("select");
		if (id != null) {
			select.addAttribute("id", id);
		}
		if (name != null) {
			select.addAttribute("name", name);
		}
		final ListWidget optionWidgets = new ListWidget();

		if (placeholder != null) {
			final TagWidget optionWidget = new TagWidget("option");
			optionWidget.addContent(placeholder);
			optionWidgets.add(optionWidget);
		}

		for (final Option option : options) {
			final TagWidget optionWidget = new TagWidget("option");
			optionWidget.addAttribute("value", option.getValue());
			optionWidget.addContent(option.getName());
			if (value != null && option.getValue().equals(value)) {
				optionWidget.addAttribute("selected", "selected");
			}
			optionWidgets.add(optionWidget);
		}

		select.addContent(optionWidgets);

		widgets.add(select);
		widgets.add(new BrWidget());
		return widgets;
	}

}
