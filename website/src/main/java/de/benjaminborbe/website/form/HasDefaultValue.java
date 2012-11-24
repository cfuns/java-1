package de.benjaminborbe.website.form;

public interface HasDefaultValue<E extends FormElementWidget> {

	E addDefaultValue(final Object value);

	String getDefaultValue();
}
