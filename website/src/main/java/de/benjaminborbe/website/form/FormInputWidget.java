package de.benjaminborbe.website.form;

public interface FormInputWidget extends FormElementWidget {

	FormInputBaseWidget addLabel(final String label);

	public FormInputBaseWidget addDefaultValue(final String defaultValue);

	FormInputBaseWidget addType(final String type);

	FormInputBaseWidget addPlaceholder(final String placeholder);

	FormInputBaseWidget addId(final String id);

	String getName();
}
