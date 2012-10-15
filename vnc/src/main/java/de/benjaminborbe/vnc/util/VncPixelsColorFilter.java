package de.benjaminborbe.vnc.util;

import de.benjaminborbe.vnc.api.VncPixels;

public class VncPixelsColorFilter extends VncPixelsAdapter implements VncPixels {

	private final int mask;

	public VncPixelsColorFilter(final VncPixels vncPixels, final int mask) {
		super(vncPixels);
		this.mask = mask;
	}

	@Override
	public int getPixel(final int x, final int y) {
		return super.getPixel(x, y) & mask;
	}

}
