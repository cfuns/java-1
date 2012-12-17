package de.benjaminborbe.messageservice.api;

public interface MessageService {

	void sendMessage(Message message) throws MessageServiceException;

	void sendMessage(String type, String content) throws MessageServiceException;
}
