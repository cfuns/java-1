package de.benjaminborbe.vnc.xmpp;

import org.slf4j.Logger;

import com.google.inject.Inject;

import de.benjaminborbe.tools.util.ParseException;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.vnc.VncConstants;
import de.benjaminborbe.vnc.api.VncService;
import de.benjaminborbe.xmpp.api.XmppChat;
import de.benjaminborbe.xmpp.api.XmppChatException;
import de.benjaminborbe.xmpp.api.XmppCommand;

public class VncServiceColorPickerXmppCommand extends VncServiceXmppCommandBase implements XmppCommand {

	private final Logger logger;

	private final VncService vncService;

	private final ParseUtil parseUtil;

	@Inject
	public VncServiceColorPickerXmppCommand(final Logger logger, final VncService vncService, final ParseUtil parseUtil) {
		super(logger);
		this.logger = logger;
		this.vncService = vncService;
		this.parseUtil = parseUtil;
	}

	@Override
	public String getName() {
		return VncConstants.NAME + " colorpick";
	}

	@Override
	public void execute(final XmppChat chat, final String command) {
		logger.debug("execute command " + getName());
		try {
			try {
				chat.send(getName() + " - execution started");

				vncService.connect();

				final String args = parseArgs(command);
				logger.debug(args);
				final String[] parts = args.split("\\s+");
				if (parts.length == 2) {
					int x = -1;
					int y = -1;
					try {
						x = parseUtil.parseInt(parts[0]);
						y = parseUtil.parseInt(parts[1]);
					}
					catch (final ParseException e) {
						x = vncService.getScreenContent().getPointerLocation().getX();
						y = vncService.getScreenContent().getPointerLocation().getY();
					}
					try {
						Thread.sleep(1000);
					}
					catch (final InterruptedException e1) {
					}

					final int pixel = vncService.getScreenContent().getPixel(x, y);
					final String msg = "pixel(" + x + "," + y + ") = 0x" + Integer.toHexString(pixel) + " int: " + pixel;
					logger.debug(msg);
					chat.send(getName() + " - " + msg);
				}
				else {
					send(chat, "usage: " + getName() + " [x] [y]");
				}

				chat.send(getName() + " - execution finished");
			}
			finally {
				vncService.disconnect();
			}

		}
		catch (final XmppChatException e) {
			logger.debug(e.getClass().getName(), e);
		}
		catch (final Exception e) {
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
