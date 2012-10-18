package de.benjaminborbe.wow.xmpp.action;

import org.slf4j.Logger;

import de.benjaminborbe.tools.util.ThreadResult;
import de.benjaminborbe.vnc.api.VncService;
import de.benjaminborbe.vnc.api.VncServiceException;

public class WowMouseClickAction extends WowActionBase {

	private final Logger logger;

	private final VncService vncService;

	public WowMouseClickAction(final Logger logger, final VncService vncService, final String name, final ThreadResult<Boolean> running) {
		super(logger, name, running);
		this.logger = logger;
		this.vncService = vncService;
	}

	@Override
	public void execute() {
		logger.debug(name + " - execute started");
		try {
			Thread.sleep(100);
			vncService.mouseLeftButtonPress();
			Thread.sleep(100);
			vncService.mouseLeftButtonRelease();
			Thread.sleep(100);
		}
		catch (final VncServiceException e) {
			logger.debug(e.getClass().getName(), e);
		}
		catch (final InterruptedException e) {
			logger.debug(e.getClass().getName(), e);
		}
		logger.debug(name + " - execute finished");
	}

}
