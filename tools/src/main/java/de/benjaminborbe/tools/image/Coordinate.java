package de.benjaminborbe.tools.image;

public class Coordinate {

	private int x;

	private int y;

	public Coordinate(final int x, final int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public void setX(final int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(final int y) {
		this.y = y;
	}

	@Override
	public boolean equals(final Object other) {
		if (this == other)
			return true;
		if (other == null)
			return false;
		if (!(other instanceof Coordinate))
			return false;
		final Coordinate otherLocation = (Coordinate) other;
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
