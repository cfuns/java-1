package de.benjaminborbe.websearch.configuration;

import java.net.URL;

import de.benjaminborbe.storage.tools.Entity;

public class ConfigurationBean implements Entity<Long>, Configuration {

	private static final long serialVersionUID = -8884906884511991833L;

	private Long id;

	private URL url;

	private String ownerUsername;

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(final Long id) {
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
