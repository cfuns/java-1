package de.benjaminborbe.vnc.core.connector;

import javax.inject.Inject;
import javax.inject.Singleton;
import de.benjaminborbe.vnc.api.VncLocation;
import org.slf4j.Logger;

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

	@Override
	public boolean equals(final Object other) {
		if (this == other)
			return true;
		if (other == null)
			return false;
		if (!(other instanceof VncLocation))
			return false;
		final VncLocation otherLocation = (VncLocation) other;
		return x == otherLocation.getX() && y == otherLocation.getY();
	}

	@Override
	public int hashCode() {
		return x * y;
	}

	@Override
	public String toString() {
		return "x: " + x + " y: " + y;
	}

}
