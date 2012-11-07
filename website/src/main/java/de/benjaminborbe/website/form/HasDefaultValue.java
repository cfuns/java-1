package de.benjaminborbe.website.form;

public interface HasDefaultValue<E extends FormElementWidget> {

	E addDefaultValue(final String value);

	String getDefaultValue();
}
