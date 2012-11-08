package de.benjaminborbe.website.form;

public interface HasOnClick<E extends FormElementWidget> {

	E addOnClick(final String onClick);

	String getOnClick();
}
