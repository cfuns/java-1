package de.benjaminborbe.wow.core.xmpp.action;

import de.benjaminborbe.tools.util.ThreadResult;
import de.benjaminborbe.vnc.api.VncPixels;
import de.benjaminborbe.vnc.api.VncService;
import de.benjaminborbe.vnc.api.VncServiceException;
import org.slf4j.Logger;

public class WowTakeScreenshotAction extends WowActionBase {

	private final Logger logger;

	private final VncService vncService;

	private final String filename;

	private boolean executed = false;

	public WowTakeScreenshotAction(final Logger logger, final VncService vncService, final String name, final String filename, final ThreadResult<Boolean> running) {
		super(logger, name, running);
		this.logger = logger;
		this.vncService = vncService;
		this.filename = filename;
	}

	@Override
	public void executeOnce() {
		logger.debug(name + " - execute started");
		try {
			final VncPixels pixels = vncService.getScreenContent().getPixels().getCopy();
			vncService.storeVncPixels(pixels, filename);
			executed = true;
		} catch (final VncServiceException e) {
			logger.debug(e.getClass().getName(), e);
		}
		logger.debug(name + " - execute finished");
	}

	@Override
	public boolean validateExecuteResult() {
		logger.debug(name + " - validateExecuteResult");
		return executed && super.validateExecuteResult();
	}
}
