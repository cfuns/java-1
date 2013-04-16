package de.benjaminborbe.vnc.core.xmpp;

import com.google.inject.Inject;
import de.benjaminborbe.tools.util.ParseException;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.vnc.api.VncService;
import de.benjaminborbe.vnc.api.VncServiceException;
import de.benjaminborbe.vnc.core.VncConstants;
import de.benjaminborbe.xmpp.api.XmppChat;
import de.benjaminborbe.xmpp.api.XmppChatException;
import de.benjaminborbe.xmpp.api.XmppCommand;
import org.slf4j.Logger;

public class VncServiceMouseMoveXmppCommand extends VncServiceXmppCommandBase implements XmppCommand {

	private final Logger logger;

	private final VncService vncService;

	private final ParseUtil parseUtil;

	@Inject
	public VncServiceMouseMoveXmppCommand(final Logger logger, final VncService vncService, final ParseUtil parseUtil) {
		super(logger);
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
			final String args = command.substring(command.indexOf(getName()) + getName().length() + 1);
			logger.debug(args);
			final String[] parts = args.split("\\s+");
			if (parts.length == 2) {
				try {
					vncService.connect();
					final int x = parseUtil.parseInt(parts[0].trim());
					final int y = parseUtil.parseInt(parts[1].trim());
					vncService.mouseMouse(x, y);
					send(chat, "moved mouse to " + x + " " + y);
				} catch (final ParseException e) {
					send(chat, "parse parameter failed");
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
