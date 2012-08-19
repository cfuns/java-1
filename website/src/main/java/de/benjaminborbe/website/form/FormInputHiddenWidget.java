package de.benjaminborbe.website.form;

import de.benjaminborbe.website.util.SingleTagWidget;

public class FormInputHiddenWidget extends SingleTagWidget implements FormElementWidget {

	public FormInputHiddenWidget(final String value) {
		super("input");
		addAttribute("type", "hidden");
		addAttribute("value", value);
	}

	public String getValue() {
		return getAttribute("value");
	}

}
