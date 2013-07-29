package de.benjaminborbe.selenium.core.util;

import de.benjaminborbe.selenium.api.SeleniumExecutionProtocol;

import java.util.ArrayList;
import java.util.List;

public class SeleniumExecutionProtocolImpl implements SeleniumExecutionProtocol {

	private final List<String> messages = new ArrayList<String>();

	private final List<String> infos = new ArrayList<String>();

	private final List<String> errors = new ArrayList<String>();

	private boolean completed;

	public List<String> getInfos() {
		return infos;
	}

	public List<String> getErrors() {
		return errors;
	}

	public boolean isCompleted() {
		return completed;
	}

	private void addMessage(final String message) {
		messages.add(message);
	}

	@Override
	public List<String> getMessages() {
		return messages;
	}

	public void addInfo(final String message) {
		infos.add(message);
		addMessage(message);
	}

	public void addError(final String message) {
		errors.add(message);
		addMessage(message);
	}

	public boolean hasErrors() {
		return !errors.isEmpty();
	}

	public void complete() {
		completed = true;
	}
}
