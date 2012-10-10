package de.benjaminborbe.configuration.api;

public interface ConfigurationDescription {

	ConfigurationIdentifier getId();

	String getName();

	String getDescription();

	String getDefaultValueAsString();

	String getType();
}
