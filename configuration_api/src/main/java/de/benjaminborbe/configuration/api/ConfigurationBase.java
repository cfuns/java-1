package de.benjaminborbe.configuration.api;

public abstract class ConfigurationBase<T> implements Configuration<T> {

	private final T defaultValue;

	private final String name;

	private final String description;

	public ConfigurationBase(final T defaultValue, final String name, final String description) {
		this.defaultValue = defaultValue;
		this.name = name;
		this.description = description;
	}

	@Override
	public abstract Class<T> getType();

	@Override
	public T getDefaultValue() {
		return defaultValue;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getDescription() {
		return description;
	}

}
