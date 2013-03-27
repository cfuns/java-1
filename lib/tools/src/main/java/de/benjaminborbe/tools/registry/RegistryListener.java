package de.benjaminborbe.tools.registry;

public interface RegistryListener<T> extends Registry<T> {

	void addListener(final RegistryChangeListener<T> registryChangeListener);

	void removeListener(final RegistryChangeListener<T> registryChangeListener);

}
