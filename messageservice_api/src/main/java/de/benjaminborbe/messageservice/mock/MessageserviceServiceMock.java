package de.benjaminborbe.messageservice.mock;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.messageservice.api.MessageserviceService;

@Singleton
public class MessageserviceServiceMock implements MessageserviceService {

	@Inject
	public MessageserviceServiceMock() {
	}

	@Override
	public void execute() {
	}
}
