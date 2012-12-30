package de.benjaminborbe.message.mock;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.message.api.Message;
import de.benjaminborbe.message.api.MessageService;
import de.benjaminborbe.message.api.MessageServiceException;

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

	@Override
	public boolean unlockExpiredMessages() {
		return false;
	}

}
