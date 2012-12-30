package de.benjaminborbe.tools.guice;

import com.google.inject.Provider;

public class ProviderAdapter<T> implements Provider<T> {

	private final T object;

	public ProviderAdapter(final T object) {
		this.object = object;
	}

	@Override
	public T get() {
		return object;
	}
}
