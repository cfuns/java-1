package de.benjaminborbe.website.form;

public interface HasId<E extends FormElementWidget> {

	E addId(final String id);

	String getId();

}
