package de.benjaminborbe.tools.registry;

import java.util.Collection;

public interface Registry<T> {

	void add(T object);

	void remove(T object);

	Collection<T> getAll();
}
