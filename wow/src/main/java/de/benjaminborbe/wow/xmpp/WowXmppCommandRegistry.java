package de.benjaminborbe.wow.xmpp;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.tools.registry.RegistryBase;
import de.benjaminborbe.xmpp.api.XmppCommand;

@Singleton
public class WowXmppCommandRegistry extends RegistryBase<XmppCommand> {

	@Inject
	public WowXmppCommandRegistry(final WowFishingXmppCommand wowFishingCommand, final WowLoginAction wowLoginAction, final WowLogoutAction wowLogoutAction) {
		add(wowFishingCommand, wowLoginAction, wowLogoutAction);
	}

}
