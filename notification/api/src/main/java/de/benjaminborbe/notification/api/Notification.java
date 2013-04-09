package de.benjaminborbe.notification.api;

import de.benjaminborbe.authentication.api.UserIdentifier;

public interface Notification {

	UserIdentifier getTo();

	UserIdentifier getFrom();

	NotificationTypeIdentifier getType();

	String getSubject();

	String getMessage();

}
