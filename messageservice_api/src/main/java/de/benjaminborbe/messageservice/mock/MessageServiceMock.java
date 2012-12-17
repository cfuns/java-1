package de.benjaminborbe.messageservice.mock;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.messageservice.api.Message;
import de.benjaminborbe.messageservice.api.MessageService;
import de.benjaminborbe.messageservice.api.MessageServiceException;

@Singleton
public class MessageServiceMock implements MessageService {

	@Inject
	public MessageServiceMock() {
	}

	@Override
	public void sendMessage(final Message message) {
	}

	@Override
	public void sendMessage(final String type, final String content) throws MessageServiceException {
	}

}
