package de.benjaminborbe.wow.core.bot.model;

public class Position {

	private Zone zone;

	private long x;

	private long y;

	private long z;

	public Zone getZone() {
		return zone;
	}

	public void setZone(final Zone zone) {
		this.zone = zone;
	}

	public long getX() {
		return x;
	}

	public void setX(final long x) {
		this.x = x;
	}

	public long getY() {
		return y;
	}

	public void setY(final long y) {
		this.y = y;
	}

	public long getZ() {
		return z;
	}

	public void setZ(final long z) {
		this.z = z;
	}
}
