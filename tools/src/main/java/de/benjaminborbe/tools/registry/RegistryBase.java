package de.benjaminborbe.tools.registry;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class RegistryBase<T> implements Registry<T> {

	private final Set<T> objects = new HashSet<T>();

	public RegistryBase(final T... ts) {
		for (final T t : ts) {
			add(t);
		}
	}

	@Override
	public void add(final T object) {
		objects.add(object);
	}

	@Override
	public void remove(final T object) {
		objects.remove(object);
	}

	@Override
	public Collection<T> getAll() {
		return objects;
	}

}
