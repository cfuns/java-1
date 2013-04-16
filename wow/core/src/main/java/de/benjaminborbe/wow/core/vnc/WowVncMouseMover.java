package de.benjaminborbe.wow.core.vnc;

import com.google.inject.Inject;
import de.benjaminborbe.tools.util.RandomUtil;
import de.benjaminborbe.vnc.api.VncLocation;
import de.benjaminborbe.vnc.api.VncService;
import de.benjaminborbe.vnc.api.VncServiceException;
import de.benjaminborbe.wow.core.WowConstants;
import org.slf4j.Logger;

public class WowVncMouseMover {

	private final VncService vncService;

	private final RandomUtil randomUtil;

	private final Logger logger;

	@Inject
	public WowVncMouseMover(final Logger logger, final VncService vncService, final RandomUtil randomUtil) {
		this.logger = logger;
		this.vncService = vncService;
		this.randomUtil = randomUtil;
	}

	public void mouseMouse(final VncLocation location) throws VncServiceException {
		logger.debug("mouseMouse " + location);
		mouseMouse(location.getX(), location.getY());
	}

	public void mouseMouse(final int x, final int y) throws VncServiceException {
		logger.debug("mouseMouse to x: " + x + " y: " + y + " started");
		final VncLocation currentLocation = vncService.getScreenContent().getPointerLocation();
		int currentX = currentLocation.getX();
		int currentY = currentLocation.getY();

		final int parts = randomUtil.getRandomized(WowConstants.MOUSE_MOVE_STEPS, WowConstants.MOUSE_MOVE_STEP_AMOUNT_RAMDOM);
		for (int step = 1; step <= parts; ++step) {
			if (step < parts) {
				final int stepDeltaX = randomUtil.getRandomized((x - currentX) / (parts - step + 1), WowConstants.MOUSE_MOVE_STEP_RAMDOM);
				final int stepDeltaY = randomUtil.getRandomized((y - currentY) / (parts - step + 1), WowConstants.MOUSE_MOVE_STEP_RAMDOM);

				currentX = currentX + stepDeltaX;
				currentY = currentY + stepDeltaY;
				vncService.mouseMouse(currentX, currentY);
				sleep(WowConstants.MOUSE_MOVE_STEP_DELAY);
			} else {
				vncService.mouseMouse(x, y);
			}
		}
		logger.debug("mouseMouse to x: " + x + " y: " + y + " finished");
	}

	private void sleep(final int delay) {
		try {
			Thread.sleep(delay);
		} catch (final InterruptedException e) {
		}
	}

}
