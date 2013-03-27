package de.benjaminborbe.tools.image;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class Grid {

	private final Collection<Coordinate> coordinates;

	public Grid(final Collection<Coordinate> coordinates) {
		this.coordinates = coordinates;
	}

	public Collection<Coordinate> getCoordinates(final Coordinate c1, final Coordinate c2) {
		final Set<Coordinate> result = new HashSet<Coordinate>();
		for (final Coordinate coordinate : coordinates) {
			if (isBetween(coordinate, c1, c2)) {
				result.add(coordinate);
			}
		}
		return result;
	}

	public boolean isBetween(final Coordinate coordinate, final Coordinate c1, final Coordinate c2) {
		final int maxX = Math.max(c1.getX(), c2.getX());
		final int maxY = Math.max(c1.getY(), c2.getY());
		final int minX = Math.min(c1.getX(), c2.getX());
		final int minY = Math.min(c1.getY(), c2.getY());
		return minX <= coordinate.getX() && coordinate.getX() <= maxX && minY <= coordinate.getY() && coordinate.getY() <= maxY;
	}
}
