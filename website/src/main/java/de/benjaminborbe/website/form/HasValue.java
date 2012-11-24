package de.benjaminborbe.website.form;

public interface HasValue<E extends FormElementWidget> {

	E addValue(final Object value);

	String getValue();
}
