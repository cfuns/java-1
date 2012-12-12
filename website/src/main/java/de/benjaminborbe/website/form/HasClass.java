package de.benjaminborbe.website.form;

import java.util.Collection;

import de.benjaminborbe.html.api.Widget;

public interface HasClass<E extends Widget> {

	E addClass(final String clazz);

	E removeClass(String clazz);

	Collection<String> getClasses();

}
