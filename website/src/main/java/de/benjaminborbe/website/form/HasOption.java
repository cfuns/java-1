package de.benjaminborbe.website.form;

public interface HasOption<E extends FormElementWidget> {

	E addOption(final String value, String label);

}
