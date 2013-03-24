package de.benjaminborbe.notification.dao;

import de.benjaminborbe.api.IdentifierBuilder;
import de.benjaminborbe.notification.api.NotificationTypeIdentifier;

public class NotificationTypeIdentifierBuilder implements IdentifierBuilder<String, NotificationTypeIdentifier> {

	@Override
	public NotificationTypeIdentifier buildIdentifier(final String value) {
		return new NotificationTypeIdentifier(value);
	}

}
