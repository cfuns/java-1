package de.benjaminborbe.notification.api;

import de.benjaminborbe.authentication.api.UserIdentifier;

public class NotificationDto implements Notification {

	private UserIdentifier to;

	private UserIdentifier from;

	private NotificationTypeIdentifier type;

	private String subject;

	private String message;

	@Override
	public UserIdentifier getTo() {
		return to;
	}

	public void setTo(final UserIdentifier to) {
		this.to = to;
	}

	@Override
	public UserIdentifier getFrom() {
		return from;
	}

	public void setFrom(final UserIdentifier from) {
		this.from = from;
	}

	@Override
	public NotificationTypeIdentifier getType() {
		return type;
	}

	public void setType(final NotificationTypeIdentifier type) {
		this.type = type;
	}

	@Override
	public String getSubject() {
		return subject;
	}

	public void setSubject(final String subject) {
		this.subject = subject;
	}

	@Override
	public String getMessage() {
		return message;
	}

	public void setMessage(final String message) {
		this.message = message;
	}

}
