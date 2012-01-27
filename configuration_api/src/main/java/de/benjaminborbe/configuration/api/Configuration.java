package de.benjaminborbe.configuration.api;

public interface Configuration<T> {

	String getName();

	String getDescription();

	Class<T> getType();

	T getDefaultValue();
}
