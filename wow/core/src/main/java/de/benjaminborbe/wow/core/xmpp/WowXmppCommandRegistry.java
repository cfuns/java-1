package de.benjaminborbe.wow.core.xmpp;

import de.benjaminborbe.tools.registry.RegistryBase;
import de.benjaminborbe.xmpp.api.XmppCommand;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class WowXmppCommandRegistry extends RegistryBase<XmppCommand> {

	@Inject
	public WowXmppCommandRegistry(final WowFishingXmppCommand wowFishingCommand, final WowLoginAction wowLoginAction, final WowLogoutAction wowLogoutAction) {
		add(wowFishingCommand, wowLoginAction, wowLogoutAction);
	}

}
