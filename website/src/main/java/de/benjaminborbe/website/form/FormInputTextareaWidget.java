package de.benjaminborbe.website.form;

import de.benjaminborbe.website.util.TagWidget;

public class FormInputTextareaWidget extends TagWidget implements FormInputWidget {

	public FormInputTextareaWidget(final String name) {
		super("textarea");
		addAttribute("name", name);
	}

	@Override
	public FormInputTextareaWidget addLabel(final String label) {
		return this;
	}

	@Override
	public FormInputTextareaWidget addDefaultValue(final String defaultValue) {
		return this;
	}

	@Override
	public FormInputTextareaWidget addPlaceholder(final String placeholder) {
		return this;
	}

	@Override
	public FormInputTextareaWidget addId(final String id) {
		return this;
	}

	@Override
	public String getName() {
		return getAttribute("name");
	}

}
