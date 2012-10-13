package de.benjaminborbe.tools.registry;

import java.util.HashSet;
import java.util.Set;

public class RegistryListenerBase<T> extends RegistryBase<T> implements RegistryListener<T> {

	private final Set<RegistryChangeListener<T>> changeListener = new HashSet<RegistryChangeListener<T>>();

	@Override
	public void addListener(final RegistryChangeListener<T> registryChangeListener) {
		changeListener.add(registryChangeListener);
	}

	@Override
	public void removeListener(final RegistryChangeListener<T> registryChangeListener) {
		changeListener.remove(registryChangeListener);
	}

	@Override
	public void add(final T object) {
		super.add(object);
		for (final RegistryChangeListener<T> listener : changeListener) {
			listener.onAdd(object);
		}
	}

	@Override
	public void remove(final T object) {
		super.remove(object);
		for (final RegistryChangeListener<T> listener : changeListener) {
			listener.onRemove(object);
		}
	}

}
