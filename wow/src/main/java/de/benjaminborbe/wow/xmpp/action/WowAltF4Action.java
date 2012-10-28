package de.benjaminborbe.wow.xmpp.action;

import org.slf4j.Logger;

import de.benjaminborbe.tools.util.ThreadResult;
import de.benjaminborbe.vnc.api.VncKey;
import de.benjaminborbe.vnc.api.VncService;
import de.benjaminborbe.vnc.api.VncServiceException;

public class WowAltF4Action extends WowActionBase {

	private final Logger logger;

	private final VncService vncService;

	private boolean executed;

	public WowAltF4Action(final Logger logger, final String name, final ThreadResult<Boolean> running, final VncService vncService) {
		super(logger, name, running);
		this.logger = logger;
		this.vncService = vncService;
	}

	@Override
	public void executeOnce() {
		logger.debug(name + " - execute started");
		try {
			vncService.keyPress(VncKey.K_ALT_LEFT);
			vncService.keyPress(VncKey.K_F4);
			vncService.keyRelease(VncKey.K_ALT_LEFT);
			vncService.keyRelease(VncKey.K_F4);
			executed = true;
		}
		catch (final VncServiceException e) {
			logger.debug(e.getClass().getName(), e);
		}
		logger.debug(name + " - execute finished");

	}

	@Override
	public boolean validateExecuteResult() {
		return executed && super.validateExecuteResult();
	}

}
