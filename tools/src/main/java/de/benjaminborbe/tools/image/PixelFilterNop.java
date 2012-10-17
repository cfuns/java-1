package de.benjaminborbe.tools.image;

public class PixelFilterNop implements PixelFilter {

	@Override
	public int filter(final int pixel) {
		return pixel;
	}
}
