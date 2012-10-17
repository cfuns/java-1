package de.benjaminborbe.tools.image;

public class PixelFilterMask implements PixelFilter {

	private final int mask;

	public PixelFilterMask(final int mask) {
		this.mask = mask;
	}

	@Override
	public int filter(final int pixel) {
		return pixel & mask;
	}
}
