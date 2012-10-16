package de.benjaminborbe.vnc.util;

public class VncPixel {

	private final int pixel;

	public VncPixel(final int pixel) {
		this.pixel = pixel;
	}

	public int getPixel() {
		return pixel;
	}

	public int getRed() {
		return pixel >> 16 & 0xFF;
	}

	public int getGreen() {
		return pixel >> 8 & 0xFF;
	}

	public int getBlue() {
		return pixel & 0xFF;
	}

	public int getGrey() {
		return (getRed() + getBlue() + getGreen()) / 3;
		// final int c = (getRed() + getBlue() + getGreen()) / 3;
		// return c | c << 8 | c << 16;
	}

	public boolean isRed() {
		final int r = getRed();
		final int g = getGreen();
		final int b = getBlue();
		return r > b && r > g;
	}

	public boolean isBlue() {
		final int r = getRed();
		final int g = getGreen();
		final int b = getBlue();
		return b > r && b > g;
	}

	public boolean isGreen() {
		final int r = getRed();
		final int g = getGreen();
		final int b = getBlue();
		return g > b && g > r;
	}
}
