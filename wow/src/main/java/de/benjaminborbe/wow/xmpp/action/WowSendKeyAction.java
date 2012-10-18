package de.benjaminborbe.wow.xmpp.action;

import org.slf4j.Logger;

import de.benjaminborbe.tools.util.ThreadResult;
import de.benjaminborbe.vnc.api.VncKey;
import de.benjaminborbe.vnc.api.VncService;
import de.benjaminborbe.vnc.api.VncServiceException;

public class WowSendKeyAction extends WowActionBase {

	private final VncKey key;

	private final VncService vncService;

	private final Logger logger;

	public WowSendKeyAction(final Logger logger, final VncService vncService, final String name, final ThreadResult<Boolean> running, final VncKey key) {
		super(logger, name, running);
		this.logger = logger;
		this.vncService = vncService;
		this.key = key;
	}

	@Override
	public void execute() {
		logger.debug(name + " - execute started");
		try {
			vncService.keyPress(key);
			Thread.sleep(100);
			vncService.keyRelease(key);
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
