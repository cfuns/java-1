package de.benjaminborbe.confluence.dao;

import java.util.Calendar;

import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.confluence.api.ConfluenceInstance;
import de.benjaminborbe.confluence.api.ConfluenceInstanceIdentifier;
import de.benjaminborbe.storage.tools.Entity;
import de.benjaminborbe.storage.tools.HasCreated;
import de.benjaminborbe.storage.tools.HasModified;

public class ConfluenceInstanceBean implements Entity<ConfluenceInstanceIdentifier>, ConfluenceInstance, HasCreated, HasModified {

	private static final long serialVersionUID = -8803301003126328406L;

	private ConfluenceInstanceIdentifier id;

	private String url;

	private String username;

	private String password;

	private Calendar created;

	private Calendar modified;

	private Integer expire;

	private Boolean shared;

	private UserIdentifier owner;

	private Long delay;

	private Boolean activated;

	@Override
	public ConfluenceInstanceIdentifier getId() {
		return id;
	}

	@Override
	public void setId(final ConfluenceInstanceIdentifier id) {
		this.id = id;
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
	public Calendar getModified() {
		return modified;
	}

	@Override
	public void setModified(final Calendar modified) {
		this.modified = modified;
	}

	@Override
	public String getUrl() {
		return url;
	}

	public void setUrl(final String url) {
		this.url = url;
	}

	@Override
	public String getUsername() {
		return username;
	}

	public void setUsername(final String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(final String password) {
		this.password = password;
	}

	@Override
	public Integer getExpire() {
		return expire;
	}

	public void setExpire(final Integer expire) {
		this.expire = expire;
	}

	public UserIdentifier getOwner() {
		return owner;
	}

	public void setOwner(final UserIdentifier owner) {
		this.owner = owner;
	}

	@Override
	public Boolean getShared() {
		return shared;
	}

	public void setShared(final Boolean shared) {
		this.shared = shared;
	}

	@Override
	public Long getDelay() {
		return delay;
	}

	public void setDelay(final Long delay) {
		this.delay = delay;
	}

	@Override
	public Boolean getActivated() {
		return activated;
	}

	public void setActivated(final Boolean activated) {
		this.activated = activated;
	}

}
