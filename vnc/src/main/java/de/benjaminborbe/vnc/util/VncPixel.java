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
		final int mask = 0xFFFF00;
		final int r = pixel & mask;
		return r >> 16;
	}

	public int getGreen() {
		final int mask = 0x00FF00;
		final int r = pixel & mask;
		return r >> 8;
	}

	public int getBlue() {
		final int mask = 0x0000FF;
		final int r = pixel & mask;
		return r;
	}
}
