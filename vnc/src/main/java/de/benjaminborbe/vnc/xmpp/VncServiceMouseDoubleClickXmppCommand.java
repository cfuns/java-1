package de.benjaminborbe.vnc.xmpp;

import org.slf4j.Logger;

import com.google.inject.Inject;

import de.benjaminborbe.vnc.VncConstants;
import de.benjaminborbe.vnc.api.VncService;
import de.benjaminborbe.vnc.api.VncServiceException;
import de.benjaminborbe.xmpp.api.XmppChat;
import de.benjaminborbe.xmpp.api.XmppChatException;
import de.benjaminborbe.xmpp.api.XmppCommand;

public class VncServiceMouseDoubleClickXmppCommand extends VncServiceXmppCommandBase implements XmppCommand {

	private final Logger logger;

	private final VncService vncService;

	@Inject
	public VncServiceMouseDoubleClickXmppCommand(final Logger logger, final VncService vncService) {
		super(logger);
		this.logger = logger;
		this.vncService = vncService;
	}

	@Override
	public String getName() {
		return VncConstants.NAME + " mouse doubleclick";
	}

	@Override
	public void execute(final XmppChat chat, final String command) {
		logger.debug("execute command " + getName());
		try {
			try {
				chat.send(getName() + " - execution started");

				vncService.connect();

				vncService.mouseLeftButtonPress();
				vncService.mouseLeftButtonRelease();
				vncService.mouseLeftButtonPress();
				vncService.mouseLeftButtonRelease();

				chat.send(getName() + " - execution finished");
			}
			finally {
				vncService.disconnect();
			}

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
