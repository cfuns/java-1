package de.benjaminborbe.wow.core.xmpp.action;

import de.benjaminborbe.tools.util.ThreadResult;
import de.benjaminborbe.vnc.api.VncPixels;
import de.benjaminborbe.vnc.api.VncService;
import de.benjaminborbe.vnc.api.VncServiceException;
import org.slf4j.Logger;

public class WowTakePixelsAction extends WowActionBase {

	private final ThreadResult<VncPixels> pixelsBeforeFishing;

	private final Logger logger;

	private final VncService vncService;

	public WowTakePixelsAction(
		final Logger logger,
		final VncService vncService,
		final String name,
		final ThreadResult<Boolean> running,
		final ThreadResult<VncPixels> pixelsBeforeFishing) {
		super(logger, name, running);
		this.logger = logger;
		this.vncService = vncService;
		this.pixelsBeforeFishing = pixelsBeforeFishing;
	}

	@Override
	public void executeOnce() {
		logger.debug(name + " - execute started");
		try {
			pixelsBeforeFishing.set(vncService.getScreenContent().getPixels().getCopy());
		} catch (final VncServiceException e) {
			logger.debug(e.getClass().getName(), e);
		}
		logger.debug(name + " - execute finished");
	}

	@Override
	public boolean validateExecuteResult() {
		logger.debug(name + " - validateExecuteResult");
		return super.validateExecuteResult() && pixelsBeforeFishing.get() != null;
	}
}
