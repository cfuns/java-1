package de.benjaminborbe.website.form;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.website.util.ListWidget;
import de.benjaminborbe.website.util.TagWidget;
import de.benjaminborbe.website.widget.BrWidget;

public class FormSelectboxWidget implements FormElementWidget, HasPaceholder<FormSelectboxWidget>, HasId<FormSelectboxWidget>, HasOption<FormSelectboxWidget>,
		HasLabel<FormSelectboxWidget>, HasValue<FormSelectboxWidget>, HasName<FormSelectboxWidget> {

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

	private String value;

	private String placeholder;

	public FormSelectboxWidget(final String name) {
		this.name = name;
	}

	@Override
	public void render(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException {
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
			if (option.getValue().equals(value)) {
				optionWidget.addAttribute("selected", "selected");
			}
			optionWidgets.add(optionWidget);
		}

		select.addContent(optionWidgets);

		widgets.add(select);
		widgets.add(new BrWidget());
		widgets.render(request, response, context);
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
	public FormSelectboxWidget addValue(final String value) {
		this.value = value;
		return this;
	}

	@Override
	public String getValue() {
		return value;
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

}
