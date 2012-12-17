package de.benjaminborbe.messageservice.api;

public interface MessageConsumer {

	String getType();

	boolean process(Message message);
}
