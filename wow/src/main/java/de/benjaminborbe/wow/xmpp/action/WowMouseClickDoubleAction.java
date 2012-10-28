package de.benjaminborbe.wow.xmpp.action;

import org.slf4j.Logger;

import de.benjaminborbe.tools.util.ThreadResult;
import de.benjaminborbe.vnc.api.VncService;
import de.benjaminborbe.vnc.api.VncServiceException;

public class WowMouseClickDoubleAction extends WowActionBase {

	private final Logger logger;

	private final VncService vncService;

	private boolean executed = false;

	public WowMouseClickDoubleAction(final Logger logger, final VncService vncService, final String name, final ThreadResult<Boolean> running) {
		super(logger, name, running);
		this.logger = logger;
		this.vncService = vncService;
	}

	@Override
	public void executeOnce() {
		logger.debug(name + " - execute started");
		try {
			vncService.mouseLeftClickDouble();
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
