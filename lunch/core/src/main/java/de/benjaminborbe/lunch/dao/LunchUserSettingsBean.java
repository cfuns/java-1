package de.benjaminborbe.lunch.dao;

import java.util.Calendar;

import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.storage.tools.EntityBase;
import de.benjaminborbe.storage.tools.HasCreated;
import de.benjaminborbe.storage.tools.HasModified;

public class LunchUserSettingsBean extends EntityBase<LunchUserSettingsIdentifier> implements HasCreated, HasModified {

	private static final long serialVersionUID = -8803301003126328406L;

	private LunchUserSettingsIdentifier id;

	private Calendar created;

	private Calendar modified;

	private Boolean notificationActivated;

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
	public LunchUserSettingsIdentifier getId() {
		return id;
	}

	@Override
	public void setId(final LunchUserSettingsIdentifier id) {
		this.id = id;
	}

	public Boolean getNotificationActivated() {
		return notificationActivated;
	}

	public void setNotificationActivated(final Boolean notificationActivated) {
		this.notificationActivated = notificationActivated;
	}

	public UserIdentifier getOwner() {
		return new UserIdentifier(id.getId());
	}

}
