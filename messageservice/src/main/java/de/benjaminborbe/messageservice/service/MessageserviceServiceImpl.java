package de.benjaminborbe.messageservice.service;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.messageservice.api.MessageserviceService;

@Singleton
public class MessageserviceServiceImpl implements MessageserviceService {

	private final Logger logger;

	@Inject
	public MessageserviceServiceImpl(final Logger logger) {
		this.logger = logger;
	}

	@Override
	public void execute() {
		logger.trace("execute");
	}

}
