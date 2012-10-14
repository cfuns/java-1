package de.benjaminborbe.vnc.connector;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.vnc.api.VncLocation;

@Singleton
public class VncPointerLocation implements VncLocation {

	private final Logger logger;

	@Inject
	public VncPointerLocation(final Logger logger) {
		this.logger = logger;
	}

	private int x;

	private int y;

	@Override
	public int getX() {
		return x;
	}

	@Override
	public int getY() {
		return y;
	}

	public synchronized void set(final short x, final short y) {
		logger.trace("set pointer to x: " + x + " y: " + y);
		this.x = x;
		this.y = y;
	}

}
