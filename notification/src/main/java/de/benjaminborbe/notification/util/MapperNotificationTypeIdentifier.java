package de.benjaminborbe.notification.util;

import de.benjaminborbe.notification.api.NotificationTypeIdentifier;
import de.benjaminborbe.tools.mapper.Mapper;

public class MapperNotificationTypeIdentifier implements Mapper<NotificationTypeIdentifier> {

	@Override
	public String toString(final NotificationTypeIdentifier value) {
		return value != null ? value.getId() : null;
	}

	@Override
	public NotificationTypeIdentifier fromString(final String value) {
		return value != null ? new NotificationTypeIdentifier(value) : null;
	}

}
