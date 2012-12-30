package de.benjaminborbe.message.api;

public interface MessageConsumer {

	String getType();

	boolean process(Message message);
}
