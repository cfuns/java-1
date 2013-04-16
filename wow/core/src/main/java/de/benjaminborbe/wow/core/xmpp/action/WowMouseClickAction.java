package de.benjaminborbe.wow.core.xmpp.action;

import de.benjaminborbe.tools.util.ThreadResult;
import de.benjaminborbe.vnc.api.VncService;
import de.benjaminborbe.vnc.api.VncServiceException;
import org.slf4j.Logger;

public class WowMouseClickAction extends WowActionBase {

	private final Logger logger;

	private final VncService vncService;

	private boolean executed = false;

	public WowMouseClickAction(final Logger logger, final VncService vncService, final String name, final ThreadResult<Boolean> running) {
		super(logger, name, running);
		this.logger = logger;
		this.vncService = vncService;
	}

	@Override
	public void executeOnce() {
		logger.debug(name + " - execute started");
		try {
			vncService.mouseLeftClick();
			executed = true;
		} catch (final VncServiceException e) {
			logger.debug(e.getClass().getName(), e);
		}
		logger.debug(name + " - execute finished");
	}

	@Override
	public boolean validateExecuteResult() {
		return executed && super.validateExecuteResult();
	}
}
