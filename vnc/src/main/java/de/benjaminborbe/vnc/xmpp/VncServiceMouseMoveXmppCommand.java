package de.benjaminborbe.vnc.xmpp;

import org.slf4j.Logger;

import com.google.inject.Inject;

import de.benjaminborbe.tools.util.ParseException;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.vnc.VncConstants;
import de.benjaminborbe.vnc.api.VncService;
import de.benjaminborbe.vnc.api.VncServiceException;
import de.benjaminborbe.xmpp.api.XmppChat;
import de.benjaminborbe.xmpp.api.XmppChatException;
import de.benjaminborbe.xmpp.api.XmppCommand;

public class VncServiceMouseMoveXmppCommand implements XmppCommand {

	private final Logger logger;

	private final VncService vncService;

	private final ParseUtil parseUtil;

	@Inject
	public VncServiceMouseMoveXmppCommand(final Logger logger, final VncService vncService, final ParseUtil parseUtil) {
		this.logger = logger;
		this.vncService = vncService;
		this.parseUtil = parseUtil;
	}

	@Override
	public String getName() {
		return VncConstants.NAME + " mouse move";
	}

	@Override
	public void execute(final XmppChat chat, final String command) {
		logger.debug("execute command " + getName());
		try {
			send(chat, "execution started");

			final String[] parts = command.substring(command.indexOf(getName())).split("\\s+");
			if (parts.length == 2) {
				try {
					vncService.mouseMouse(parseUtil.parseInt(parts[0]), parseUtil.parseInt(parts[1]));
				}
				catch (final ParseException e) {
					send(chat, "parse parameter failed");
				}
			}
			else {
				send(chat, "usage: " + getName() + " [x] [y]");
			}

			send(chat, "execution finished");
		}
		catch (final XmppChatException e) {
			logger.debug(e.getClass().getName(), e);
		}
		catch (final VncServiceException e) {
			try {
				send(chat, "execution failed! " + e.getClass().getName());
			}
			catch (final XmppChatException e1) {
				logger.debug(e.getClass().getName(), e);
			}
			logger.debug(e.getClass().getName(), e);
		}
	}

	private void send(final XmppChat chat, final String string) throws XmppChatException {
		chat.send(getName() + " - " + string);
	}

	@Override
	public boolean match(final String body) {
		return body != null && body.indexOf(getName()) != -1;
	}
}
