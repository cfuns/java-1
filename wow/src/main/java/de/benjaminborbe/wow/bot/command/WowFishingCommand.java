package de.benjaminborbe.wow.bot.command;

import org.slf4j.Logger;

import com.google.inject.Inject;

import de.benjaminborbe.vnc.api.VncLocation;
import de.benjaminborbe.vnc.api.VncLocationImpl;
import de.benjaminborbe.vnc.api.VncServiceException;
import de.benjaminborbe.wow.WowConstants;
import de.benjaminborbe.wow.vnc.WowVncConnector;
import de.benjaminborbe.xmpp.api.XmppChat;
import de.benjaminborbe.xmpp.api.XmppChatException;
import de.benjaminborbe.xmpp.api.XmppCommand;

public class WowFishingCommand implements XmppCommand {

	private final Logger logger;

	private final WowVncConnector vncService;

	@Inject
	public WowFishingCommand(final Logger logger, final WowVncConnector vncService) {
		this.logger = logger;
		this.vncService = vncService;
	}

	@Override
	public String getName() {
		return WowConstants.NAME + " fishing";
	}

	@Override
	public void execute(final XmppChat chat) {
		logger.debug("execute command " + getName());
		try {
			chat.send(getName() + " - execution started");

			try {
				vncService.connect();

				// determine fishing button location
				final VncLocation fishingButton = new VncLocationImpl(1350, 804);

				// move mouse to fishing button
				vncService.mouseMouse(fishingButton);

				// take screencontent => s1
				// click
				// take screencontent => s2
				// diff s1+s2 and determine position
				// move mouse
				// take screencontent of pointer area => sX
				// diff sX+sX+1 => click if diff
			}
			finally {
				vncService.disconnect();
			}

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
