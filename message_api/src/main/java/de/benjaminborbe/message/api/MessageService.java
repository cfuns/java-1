package de.benjaminborbe.message.api;

public interface MessageService {

	void sendMessage(String type, String content) throws MessageServiceException;

	void sendMessage(String type, String id, String content) throws MessageServiceException;

	boolean unlockExpiredMessages();

}
