package de.benjaminborbe.wow.vnc;

import com.google.inject.Inject;

import de.benjaminborbe.tools.util.RandomUtil;
import de.benjaminborbe.vnc.api.VncLocation;
import de.benjaminborbe.vnc.api.VncService;
import de.benjaminborbe.vnc.api.VncServiceException;

public class WowVncMouseMover {

	private final VncService vncService;

	private final int DELAY = 300;

	private final RandomUtil randomUtil;

	@Inject
	public WowVncMouseMover(final VncService vncService, final RandomUtil randomUtil) {
		this.vncService = vncService;
		this.randomUtil = randomUtil;
	}

	public void mouseMouse(final VncLocation location) throws VncServiceException {
		mouseMouse(location.getX(), location.getY());
	}

	public void mouseMouse(final int x, final int y) throws VncServiceException {
		final VncLocation currentLocation = vncService.getScreenContent().getPointerLocation();
		int currentX = currentLocation.getX();
		int currentY = currentLocation.getY();

		final int parts = randomUtil.getRandomized(5, 20);
		for (int step = 1; step <= parts; ++step) {
			if (step < parts) {
				final int stepDeltaX = randomUtil.getRandomized((x - currentX) / (parts - step + 1), 10);
				final int stepDeltaY = randomUtil.getRandomized((y - currentY) / (parts - step + 1), 10);

				currentX = currentX + stepDeltaX;
				currentY = currentY + stepDeltaY;
				vncService.mouseMouse(currentX, currentY);
				sleep(DELAY);
			}
			else {
				vncService.mouseMouse(x, y);
			}
		}
	}

	private void sleep(final int delay) {
		try {
			Thread.sleep(delay);
		}
		catch (final InterruptedException e) {
		}
	}

}
