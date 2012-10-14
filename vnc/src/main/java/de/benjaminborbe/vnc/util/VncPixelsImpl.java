package de.benjaminborbe.vnc.util;

import de.benjaminborbe.vnc.api.VncPixels;

public class VncPixelsImpl implements VncPixels {

	private final int[] pixels;

	private final int x;

	private final int y;

	private final int height;

	private final int width;

	private final int orgWidth;

	public VncPixelsImpl(final int[] pixels, final int width, final int height) {
		this(pixels, 1, 1, width, height, width);
	}

	private VncPixelsImpl(final int[] pixels, final int x, final int y, final int width, final int height, final int orgWidth) {
		this.pixels = pixels;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.orgWidth = orgWidth;
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public int getHeight() {
		return height;
	}

	@Override
	public VncPixels getSubPixel(final int x, final int y, final int width, final int height) {
		if (x > this.width) {
			throw new IllegalArgumentException("out of range");
		}
		if (y > this.height) {
			throw new IllegalArgumentException("out of range");
		}
		return new VncPixelsImpl(pixels, this.x + x - 1, this.y + y - 1, width, height, orgWidth);
	}

	@Override
	public int getPixel(final int x, final int y) {
		if (x > this.width) {
			throw new IllegalArgumentException("out of range");
		}
		if (y > this.height) {
			throw new IllegalArgumentException("out of range");
		}

		final int xn = x + this.x - 1;
		final int yn = y + this.y - 1;
		return pixels[xn - 1 + (yn - 1) * orgWidth];
	}
}
