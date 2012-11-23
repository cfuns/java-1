package de.benjaminborbe.websearch.configuration;

import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.benjaminborbe.storage.tools.Entity;
import de.benjaminborbe.storage.tools.HasCreated;
import de.benjaminborbe.storage.tools.HasModified;

public class WebsearchConfigurationBean implements Entity<WebsearchConfigurationIdentifier>, WebsearchConfiguration, HasCreated, HasModified {

	private static final long serialVersionUID = -8884906884511991833L;

	private WebsearchConfigurationIdentifier id;

	private URL url;

	private String ownerUsername;

	private List<String> excludes = new ArrayList<String>();

	private Calendar modified;

	private Calendar created;

	@Override
	public WebsearchConfigurationIdentifier getId() {
		return id;
	}

	@Override
	public void setId(final WebsearchConfigurationIdentifier id) {
		this.id = id;
	}

	@Override
	public URL getUrl() {
		return url;
	}

	public void setUrl(final URL url) {
		this.url = url;
	}

	@Override
	public String getOwnerUsername() {
		return ownerUsername;
	}

	public void setOwnerUsername(final String ownerUsername) {
		this.ownerUsername = ownerUsername;
	}

	@Override
	public List<String> getExcludes() {
		return excludes;
	}

	public void setExcludes(final List<String> excludes) {
		this.excludes = excludes;
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

}
