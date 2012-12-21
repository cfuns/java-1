package de.benjaminborbe.website.form;

import de.benjaminborbe.html.api.Widget;

public interface HasAttribute<E extends Widget> {

	E addAttribute(final String name, String value);

}
