package de.benjaminborbe.wow.core.xmpp;

import de.benjaminborbe.xmpp.api.XmppChat;
import de.benjaminborbe.xmpp.api.XmppChatException;
import de.benjaminborbe.xmpp.api.XmppCommand;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

public abstract class WowBaseXmppCommand implements XmppCommand {

	private final Logger logger;

	public WowBaseXmppCommand(final Logger logger) {
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

	protected List<String> parseArgs(final String command) {
		logger.debug("parseArgs - command: " + command);
		final List<String> result = new ArrayList<>();
		for (final String part : parseArg(command).split(" ")) {
			final String value = part.trim();
			if (value.length() > 0) {
				result.add(value);
			}
		}
		logger.debug("parseArgs - command: " + command + " args: " + StringUtils.join(result, ","));
		return result;
	}

	protected String parseArg(final String command) {
		final int pos = command.indexOf(getName()) + getName().length() + 1;
		if (pos > command.length()) {
			return "";
		} else {
			return command.substring(pos);
		}
	}

	@Override
	public void execute(final XmppChat chat, final String command) {
		logger.debug("execute command " + getName());
		try {
			executeInternal(chat, command);
		} catch (final Exception e) {
			try {
				chat.send(getName() + " - execution failed! " + e.getClass().getName());
			} catch (final XmppChatException e1) {
				logger.debug(e.getClass().getName(), e);
			}
			logger.debug(e.getClass().getName(), e);
		}
	}

	public abstract void executeInternal(final XmppChat chat, final String command) throws Exception;
}
