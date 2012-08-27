package de.benjaminborbe.website.form;

public interface FormInputWidget extends FormElementWidget {

	FormInputWidget addLabel(final String label);

	public FormInputWidget addDefaultValue(final String defaultValue);

	FormInputWidget addPlaceholder(final String placeholder);

	FormInputWidget addId(final String id);

	String getName();
}
