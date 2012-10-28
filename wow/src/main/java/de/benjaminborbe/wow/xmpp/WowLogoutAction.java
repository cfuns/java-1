package de.benjaminborbe.wow.xmpp;

import org.slf4j.Logger;

import com.google.inject.Inject;

import de.benjaminborbe.wow.WowConstants;
import de.benjaminborbe.xmpp.api.XmppChat;

public class WowLogoutAction extends WowBaseXmppCommand {

	@Inject
	public WowLogoutAction(final Logger logger) {
		super(logger);
	}

	@Override
	public String getName() {
		return WowConstants.NAME + " logout";
	}

	@Override
	public void executeInternal(final XmppChat chat, final String command) throws Exception {
	}

}
