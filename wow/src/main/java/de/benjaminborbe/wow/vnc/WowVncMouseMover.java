package de.benjaminborbe.wow.vnc;

import com.google.inject.Inject;

import de.benjaminborbe.vnc.api.VncLocation;
import de.benjaminborbe.vnc.api.VncService;
import de.benjaminborbe.vnc.api.VncServiceException;

public class WowVncMouseMover {

	private final VncService vncService;

	private final int DELAY = 300;

	@Inject
	public WowVncMouseMover(final VncService vncService) {
		this.vncService = vncService;
	}

	public void mouseMouse(final VncLocation location) throws VncServiceException {
		mouseMouse(location.getX(), location.getY());
	}

	public void mouseMouse(final int x, final int y) throws VncServiceException {
		final VncLocation currentLocation = vncService.getScreenContent().getPointerLocation();
		int currentX = currentLocation.getX();
		int currentY = currentLocation.getY();

		final int parts = 5;
		final int stepDeltaX = (currentLocation.getX() - x) / parts;
		final int stepDeltaY = (currentLocation.getY() - y) / parts;
		for (int step = 1; step < parts; ++step) {
			if (step > 1) {
				sleep(DELAY);
			}
			currentX = currentX + stepDeltaX;
			currentY = currentY + stepDeltaY;
			vncService.mouseMouse(currentX, currentY);
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
