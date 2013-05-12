package de.benjaminborbe.selenium.core.util;

import de.benjaminborbe.selenium.api.SeleniumExecutionProtocol;

import java.util.ArrayList;
import java.util.List;

public class SeleniumExecutionProtocolImpl implements SeleniumExecutionProtocol {

	private final List<String> messages = new ArrayList<>();

	public void addMessage(String message) {
		messages.add(message);
	}

	@Override
	public List<String> getMessages() {
		return messages;
	}
}
