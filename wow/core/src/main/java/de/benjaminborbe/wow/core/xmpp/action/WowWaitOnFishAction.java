package de.benjaminborbe.wow.core.xmpp.action;

import de.benjaminborbe.tools.image.Coordinate;
import de.benjaminborbe.tools.util.ThreadResult;
import de.benjaminborbe.vnc.api.VncPixels;
import de.benjaminborbe.vnc.api.VncService;
import de.benjaminborbe.vnc.api.VncServiceException;
import org.slf4j.Logger;

public class WowWaitOnFishAction extends WowActionBase {

	private final ThreadResult<Coordinate> baitLocation;

	private VncPixels vncPixelsOrg;

	private final VncService vncService;

	private final Logger logger;

	private boolean found = false;

	public WowWaitOnFishAction(
		final Logger logger,
		final VncService vncService,
		final String name,
		final ThreadResult<Boolean> running,
		final ThreadResult<Coordinate> baitLocation
	) {
		super(logger, name, running);
		this.logger = logger;
		this.vncService = vncService;
		this.baitLocation = baitLocation;
	}

	@Override
	public void executeOnce() {
		logger.debug(name + " - executeOnce started");
		try {
			vncPixelsOrg = vncService.getScreenContent().getPixels().getCopy();
		} catch (final VncServiceException e) {
			logger.debug(e.getClass().getName(), e);
		}
		logger.debug(name + " - executeOnce finished");
	}

	@Override
	public void executeRetry() {
		logger.debug(name + " - executeRetry started");
		try {

			final int now = vncService.getScreenContent().getPixels().getPixel(baitLocation.get().getX(), baitLocation.get().getY()) & 0x00FF0000;
			final int org = vncPixelsOrg.getPixel(baitLocation.get().getX(), baitLocation.get().getY()) & 0x00FF0000;
			logger.trace(Integer.toHexString(org) + "<=>" + Integer.toHexString(now) + " " + Integer.toHexString(Math.abs(now - org)));
			found = Math.abs(now - org) > 0x00400000;
		} catch (final VncServiceException e) {
			logger.debug(e.getClass().getName(), e);
		}
		logger.debug(name + " - executeRetry finished");
	}

	@Override
	public boolean validateExecuteResult() {
		logger.trace(name + " - validateExecuteResult");
		if (super.validateExecuteResult() == false)
			return false;
		if (vncPixelsOrg == null)
			return false;
		return found;
	}

	@Override
	public long getWaitTimeout() {
		return 15000;
	}

	@Override
	public long getRetryDelay() {
		return 100;
	}

	@Override
	public void onFailure() {
		logger.trace(name + " - onFailure");
		try {
			vncService.mouseLeftClick();
		} catch (final VncServiceException e) {
			logger.debug(e.getClass().getName(), e);
		}
	}

}
