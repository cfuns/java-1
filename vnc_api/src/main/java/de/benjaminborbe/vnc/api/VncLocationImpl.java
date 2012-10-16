package de.benjaminborbe.vnc.api;

public class VncLocationImpl implements VncLocation {

	private final int x;

	private final int y;

	public VncLocationImpl(final int x, final int y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public int getX() {
		return x;
	}

	@Override
	public int getY() {
		return y;
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
