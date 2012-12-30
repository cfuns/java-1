package de.benjaminborbe.websearch.dao;

import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.storage.tools.Entity;
import de.benjaminborbe.storage.tools.HasCreated;
import de.benjaminborbe.storage.tools.HasModified;
import de.benjaminborbe.websearch.api.WebsearchConfiguration;
import de.benjaminborbe.websearch.api.WebsearchConfigurationIdentifier;

public class WebsearchConfigurationBean implements Entity<WebsearchConfigurationIdentifier>, WebsearchConfiguration, HasCreated, HasModified {

	private static final long serialVersionUID = -8884906884511991833L;

	private WebsearchConfigurationIdentifier id;

	private URL url;

	private UserIdentifier owner;

	private List<String> excludes = new ArrayList<String>();

	private Calendar modified;

	private Calendar created;

	private Integer expire;

	private Long delay;

	private Boolean activated;

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

	@Override
	public UserIdentifier getOwner() {
		return owner;
	}

	public void setOwner(final UserIdentifier owner) {
		this.owner = owner;
	}

	@Override
	public Integer getExpire() {
		return expire;
	}

	public void setExpire(final Integer expire) {
		this.expire = expire;
	}

	@Override
	public Long getDelay() {
		return delay;
	}

	public void setDelay(final Long delay) {
		this.delay = delay;
	}

	public Boolean getActivated() {
		return activated;
	}

	public void setActivated(final Boolean activated) {
		this.activated = activated;
	}

}
