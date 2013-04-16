package de.benjaminborbe.vnc.core.xmpp;

import de.benjaminborbe.xmpp.api.XmppChat;
import de.benjaminborbe.xmpp.api.XmppChatException;
import de.benjaminborbe.xmpp.api.XmppCommand;
import org.slf4j.Logger;

public abstract class VncServiceXmppCommandBase implements XmppCommand {

	private final Logger logger;

	public VncServiceXmppCommandBase(final Logger logger) {
		this.logger = logger;

	}

	protected void send(final XmppChat chat, final String string) throws XmppChatException {
		chat.send(getName() + " - " + string);
		logger.debug(string);
	}

	@Override
	public boolean match(final String body) {
		return body != null && body.contains(getName());
	}

	protected String parseArgs(final String command) {
		final int pos = command.indexOf(getName()) + getName().length() + 1;
		if (pos > command.length()) {
			return "";
		}
		return command.substring(pos);
	}
}
