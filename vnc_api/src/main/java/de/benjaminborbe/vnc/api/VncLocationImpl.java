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

}
