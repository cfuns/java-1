package de.benjaminborbe.website.form;

import de.benjaminborbe.html.api.Widget;

public interface CopyOfFormInputWidget extends Widget {

	FormInputBaseWidget addLabel(final String label);

	public FormInputBaseWidget addDefaultValue(final String defaultValue);

	FormInputBaseWidget addType(final String type);

	FormInputBaseWidget addPlaceholder(final String placeholder);

	FormInputBaseWidget addId(final String id);
}
