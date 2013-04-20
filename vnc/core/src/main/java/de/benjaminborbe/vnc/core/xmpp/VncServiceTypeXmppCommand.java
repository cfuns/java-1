package de.benjaminborbe.vnc.core.xmpp;

import javax.inject.Inject;
import de.benjaminborbe.vnc.api.VncService;
import de.benjaminborbe.vnc.api.VncServiceException;
import de.benjaminborbe.vnc.core.VncConstants;
import de.benjaminborbe.xmpp.api.XmppChat;
import de.benjaminborbe.xmpp.api.XmppChatException;
import de.benjaminborbe.xmpp.api.XmppCommand;
import org.slf4j.Logger;

public class VncServiceTypeXmppCommand extends VncServiceXmppCommandBase implements XmppCommand {

	private final Logger logger;

	private final VncService vncService;

	@Inject
	public VncServiceTypeXmppCommand(final Logger logger, final VncService vncService) {
		super(logger);
		this.logger = logger;
		this.vncService = vncService;
	}

	@Override
	public String getName() {
		return VncConstants.NAME + " type";
	}

	@Override
	public void execute(final XmppChat chat, final String command) {
		logger.debug("execute command " + getName());
		try {
			send(chat, "execution started");
			final String args = command.substring(command.indexOf(getName()) + getName().length() + 1);
			logger.debug(args);
			if (args != null && args.length() > 0) {
				try {
					vncService.connect();

					vncService.keyType(args.trim());
				} finally {
					vncService.disconnect();
				}
			} else {
				send(chat, "usage: " + getName() + " [x] [y]");
			}
			send(chat, "execution finished");
		} catch (final XmppChatException e) {
			logger.debug(e.getClass().getName(), e);
		} catch (final VncServiceException e) {
			try {
				send(chat, "execution failed! " + e.getClass().getName());
			} catch (final XmppChatException e1) {
				logger.debug(e.getClass().getName(), e);
			}
			logger.debug(e.getClass().getName(), e);
		}
	}
}
