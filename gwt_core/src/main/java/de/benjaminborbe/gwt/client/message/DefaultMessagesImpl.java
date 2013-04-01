package de.benjaminborbe.gwt.client.message;

public class DefaultMessagesImpl implements DefaultMessages {

	@Override
	public String getTitle() {
		return "BB - GWT-Testapp";
	}

	@Override
	public String getWelcomeMessage() {
		return "Hello";
	}

	@Override
	public String getButtonText() {
		return "click me!";
	}

}
