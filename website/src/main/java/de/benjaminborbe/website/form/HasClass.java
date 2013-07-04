package de.benjaminborbe.website.form;

import de.benjaminborbe.html.api.Widget;

import java.util.Collection;

public interface HasClass<E extends Widget> {

	E addClass(final String clazz);

	E removeClass(String clazz);

	Collection<String> getClasses();

}
