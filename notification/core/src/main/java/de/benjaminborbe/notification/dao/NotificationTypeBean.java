package de.benjaminborbe.notification.dao;

import java.util.Calendar;

import de.benjaminborbe.notification.api.NotificationTypeIdentifier;
import de.benjaminborbe.storage.tools.EntityBase;
import de.benjaminborbe.storage.tools.HasCreated;
import de.benjaminborbe.storage.tools.HasModified;

public class NotificationTypeBean extends EntityBase<NotificationTypeIdentifier> implements HasCreated, HasModified {

	private static final long serialVersionUID = -8803301003126328406L;

	private NotificationTypeIdentifier id;

	private Calendar created;

	private Calendar modified;

	@Override
	public NotificationTypeIdentifier getId() {
		return id;
	}

	@Override
	public void setId(final NotificationTypeIdentifier id) {
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

}
