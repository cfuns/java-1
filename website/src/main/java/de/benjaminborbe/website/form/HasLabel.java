package de.benjaminborbe.website.form;

public interface HasLabel<E extends FormElementWidget> {

	E addLabel(final String label);

	String getLabel();
}
