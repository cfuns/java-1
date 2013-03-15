package de.benjaminborbe.notification.service;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.notification.api.NotificationService;

@Singleton
public class NotificationServiceImpl implements NotificationService {

	private final Logger logger;

	@Inject
	public NotificationServiceImpl(final Logger logger) {
		this.logger = logger;
	}

	@Override
	public void execute() {
		logger.trace("execute");
	}

}
