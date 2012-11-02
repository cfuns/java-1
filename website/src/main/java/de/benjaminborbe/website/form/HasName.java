package de.benjaminborbe.website.form;

public interface HasName<E extends FormElementWidget> {

	E addName(final String name);

	String getName();
}
