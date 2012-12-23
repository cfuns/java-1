package de.benjaminborbe.tools.osgi;

import java.util.Properties;

public class ServiceInfo {

	private final String name;

	private final Object service;

	private final Properties properties;

	public <T, O extends T> ServiceInfo(final Class<T> clazz, final O service, final String name) {
		this(clazz, service, buildProperties(name));
	}

	private static Properties buildProperties(final String name) {
		final Properties properties = new Properties();
		properties.put("name", name);
		return properties;
	}

	public ServiceInfo(final Class<?> clazz, final Object service, final Properties properties) {
		this.name = clazz.getName();
		this.service = service;
		this.properties = properties;
	}

	public ServiceInfo(final Class<?> clazz, final Object service) {
		this(clazz, service, new Properties());
	}

	public String getName() {
		return name;
	}

	public Object getService() {
		return service;
	}

	public Properties getProperties() {
		return properties;
	}

	@Override
	public String toString() {
		return "ServiceInfo for " + getName();
	}

}
