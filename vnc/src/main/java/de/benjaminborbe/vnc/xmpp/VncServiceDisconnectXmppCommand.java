package de.benjaminborbe.vnc.xmpp;

import org.slf4j.Logger;

import com.google.inject.Inject;

import de.benjaminborbe.vnc.VncConstants;
import de.benjaminborbe.vnc.api.VncService;
import de.benjaminborbe.vnc.api.VncServiceException;
import de.benjaminborbe.xmpp.api.XmppChat;
import de.benjaminborbe.xmpp.api.XmppChatException;
import de.benjaminborbe.xmpp.api.XmppCommand;

public class VncServiceDisconnectXmppCommand implements XmppCommand {

	private final Logger logger;

	private final VncService vncService;

	@Inject
	public VncServiceDisconnectXmppCommand(final Logger logger, final VncService vncService) {
		this.logger = logger;
		this.vncService = vncService;
	}

	@Override
	public String getName() {
		return VncConstants.NAME + " disconnect";
	}

	@Override
	public void execute(final XmppChat chat) {
		logger.debug("execute command " + getName());
		try {
			chat.send(getName() + " - execution started");

			vncService.disconnect();

			chat.send(getName() + " - execution finished");
		}
		catch (final XmppChatException e) {
			logger.debug(e.getClass().getName(), e);
		}
		catch (final VncServiceException e) {
			try {
				chat.send(getName() + " - execution failed! " + e.getClass().getName());
			}
			catch (final XmppChatException e1) {
				logger.debug(e.getClass().getName(), e);
			}
			logger.debug(e.getClass().getName(), e);
		}
	}
}
