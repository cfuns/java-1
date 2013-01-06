package de.benjaminborbe.configuration.dao;

import java.util.Calendar;

import de.benjaminborbe.configuration.api.ConfigurationIdentifier;
import de.benjaminborbe.storage.tools.EntityBase;
import de.benjaminborbe.storage.tools.HasCreated;
import de.benjaminborbe.storage.tools.HasModified;

public class ConfigurationBean extends EntityBase<ConfigurationIdentifier> implements HasCreated, HasModified {

	private static final long serialVersionUID = 8032652320006340164L;

	private ConfigurationIdentifier id;

	private String value;

	private Calendar modified;

	private Calendar created;

	public String getValue() {
		return value;
	}

	public void setValue(final String value) {
		this.value = value;
	}

	@Override
	public Calendar getModified() {
		return modified;
	}

	@Override
	public void setModified(final Calendar modified) {
		this.modified = modified;
	}

	@Override
	public Calendar getCreated() {
		return created;
	}

	@Override
	public void setCreated(final Calendar created) {
		this.created = created;
	}

	@Override
	public ConfigurationIdentifier getId() {
		return id;
	}

	@Override
	public void setId(final ConfigurationIdentifier id) {
		this.id = id;
	}

}
