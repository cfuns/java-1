package de.benjaminborbe.configuration.util;

import de.benjaminborbe.tools.dao.EntityLong;

public class ConfigurationBean implements EntityLong {

	private static final long serialVersionUID = 8032652320006340164L;

	private Long id;

	private String key;

	private String value;

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(final Long id) {
		this.id = id;
	}

	public String getKey() {
		return key;
	}

	public void setKey(final String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(final String value) {
		this.value = value;
	}

}
