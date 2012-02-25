package de.benjaminborbe.websearch.configuration;

import java.net.URL;

import de.benjaminborbe.configuration.api.ConfigurationIdentifier;
import de.benjaminborbe.storage.tools.Entity;

public class ConfigurationBean implements Entity<ConfigurationIdentifier>, Configuration {

	private static final long serialVersionUID = -8884906884511991833L;

	private ConfigurationIdentifier id;

	private URL url;

	private String ownerUsername;

	@Override
	public ConfigurationIdentifier getId() {
		return id;
	}

	@Override
	public void setId(final ConfigurationIdentifier id) {
		this.id = id;
	}

	@Override
	public URL getUrl() {
		return url;
	}

	public void setUrl(final URL url) {
		this.url = url;
	}

	public String getOwnerUsername() {
		return ownerUsername;
	}

	public void setOwnerUsername(final String ownerUsername) {
		this.ownerUsername = ownerUsername;
	}

}
