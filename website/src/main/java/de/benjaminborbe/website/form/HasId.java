package de.benjaminborbe.website.form;

import de.benjaminborbe.html.api.Widget;

public interface HasId<E extends Widget> {

	E addId(final String id);

	String getId();

}
