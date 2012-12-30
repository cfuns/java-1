package de.benjaminborbe.message.api;

public interface MessageService {

	void sendMessage(Message message) throws MessageServiceException;

	void sendMessage(String type, String content) throws MessageServiceException;

	boolean unlockExpiredMessages();
}
