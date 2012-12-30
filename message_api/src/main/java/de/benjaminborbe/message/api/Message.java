package de.benjaminborbe.message.api;

public interface Message {

	MessageIdentifier getId();

	String getType();

	String getContent();
}
