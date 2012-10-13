package de.benjaminborbe.wow.bot.command;

import org.slf4j.Logger;

import com.google.inject.Inject;

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
	public void execute() {
		logger.debug("execute command " + getName());
	}

}
