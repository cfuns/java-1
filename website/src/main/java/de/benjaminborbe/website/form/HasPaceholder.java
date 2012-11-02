package de.benjaminborbe.website.form;

public interface HasPaceholder<E extends FormElementWidget> {

	E addPlaceholder(final String placeholder);

	String getPlaceholder();
}
