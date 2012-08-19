package de.benjaminborbe.website.form;

import de.benjaminborbe.website.util.TagWidget;

public class FormInputTextareaWidget extends TagWidget implements FormInputWidget {

	public FormInputTextareaWidget(final String name) {
		super("textarea");
		addAttribute("name", name);
	}

	@Override
	public FormInputBaseWidget addLabel(final String label) {
		return null;
	}

	@Override
	public FormInputBaseWidget addDefaultValue(final String defaultValue) {
		return null;
	}

	@Override
	public FormInputBaseWidget addType(final String type) {
		return null;
	}

	@Override
	public FormInputBaseWidget addPlaceholder(final String placeholder) {
		return null;
	}

	@Override
	public FormInputBaseWidget addId(final String id) {
		return null;
	}

	@Override
	public String getName() {
		return getAttribute("name");
	}

}
