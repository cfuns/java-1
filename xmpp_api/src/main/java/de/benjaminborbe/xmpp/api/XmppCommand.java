package de.benjaminborbe.xmpp.api;

public interface XmppCommand {

	String getName();

	void execute(XmppChat chat);
}
