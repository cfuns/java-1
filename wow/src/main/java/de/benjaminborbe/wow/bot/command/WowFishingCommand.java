package de.benjaminborbe.wow.bot.command;

import org.slf4j.Logger;

import com.google.inject.Inject;

import de.benjaminborbe.xmpp.api.XmppChat;
import de.benjaminborbe.xmpp.api.XmppChatException;
import de.benjaminborbe.xmpp.api.XmppCommand;

public class WowFishingCommand implements XmppCommand {

	private final Logger logger;

	@Inject
	public WowFishingCommand(final Logger logger) {
		this.logger = logger;
	}

	@Override
	public String getName() {
		return "wow fishing";
	}

	@Override
	public void execute(final XmppChat chat) {
		logger.debug("execute command " + getName());
		try {
			chat.send(getName() + " - execution started");
			chat.send(getName() + " - execution finished");
		}
		catch (final XmppChatException e) {
			logger.debug(e.getClass().getName(), e);
		}
	}

}
