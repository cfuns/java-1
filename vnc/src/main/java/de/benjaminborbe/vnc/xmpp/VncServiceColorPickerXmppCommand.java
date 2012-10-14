package de.benjaminborbe.vnc.xmpp;

import org.slf4j.Logger;

import com.google.inject.Inject;

import de.benjaminborbe.vnc.VncConstants;
import de.benjaminborbe.vnc.api.VncService;
import de.benjaminborbe.vnc.api.VncServiceException;
import de.benjaminborbe.xmpp.api.XmppChat;
import de.benjaminborbe.xmpp.api.XmppChatException;
import de.benjaminborbe.xmpp.api.XmppCommand;

public class VncServiceColorPickerXmppCommand implements XmppCommand {

	private final Logger logger;

	private final VncService vncService;

	@Inject
	public VncServiceColorPickerXmppCommand(final Logger logger, final VncService vncService) {
		this.logger = logger;
		this.vncService = vncService;
	}

	@Override
	public String getName() {
		return VncConstants.NAME + " colorpick";
	}

	@Override
	public void execute(final XmppChat chat, final String command) {
		logger.debug("execute command " + getName());
		try {
			chat.send(getName() + " - execution started");

			final int pixel = vncService.getScreenContent().getPixel(1, 1);
			logger.debug("pixel - int: " + pixel + " hex: " + Integer.toHexString(pixel));

			chat.send(getName() + " - pixel = " + Integer.toHexString(pixel) + " int: " + pixel);

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

	@Override
	public boolean match(final String body) {
		return body != null && body.indexOf(getName()) != -1;
	}
}
