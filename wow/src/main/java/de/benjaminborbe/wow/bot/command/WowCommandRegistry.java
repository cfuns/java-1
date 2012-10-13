package de.benjaminborbe.wow.bot.command;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.tools.registry.RegistryBase;
import de.benjaminborbe.xmpp.api.XmppCommand;

@Singleton
public class WowCommandRegistry extends RegistryBase<XmppCommand> {

	@Inject
	public WowCommandRegistry(final WowFishingCommand wowFishingCommand) {
		add(wowFishingCommand);
	}

}
