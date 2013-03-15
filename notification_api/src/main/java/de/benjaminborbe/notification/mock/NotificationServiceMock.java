package de.benjaminborbe.notification.mock;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.notification.api.NotificationService;

@Singleton
public class NotificationServiceMock implements NotificationService {

	@Inject
	public NotificationServiceMock() {
	}

	@Override
	public void execute() {
	}
}
