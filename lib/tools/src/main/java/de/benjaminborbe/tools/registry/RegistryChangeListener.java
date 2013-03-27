package de.benjaminborbe.tools.registry;

public interface RegistryChangeListener<T> {

	void onAdd(T t);

	void onRemove(T t);
}
