package de.benjaminborbe.vnc.util;

import java.io.InputStream;

import de.benjaminborbe.vnc.api.VncPixels;

public class VncPixelsAdapter implements VncPixels {

	private final VncPixels vncPixels;

	public VncPixelsAdapter(final VncPixels vncPixels) {
		this.vncPixels = vncPixels;
	}

	@Override
	public VncPixels getSubPixel(final int xstart, final int ystart, final int xend, final int yend) {
		return vncPixels.getSubPixel(xstart, ystart, xend, yend);
	}

	@Override
	public int getPixel(final int x, final int y) {
		return vncPixels.getPixel(x, y);
	}

	@Override
	public int getWidth() {
		return vncPixels.getWidth();
	}

	@Override
	public int getHeight() {
		return vncPixels.getHeight();
	}

	@Override
	public InputStream getInputStream() {
		return vncPixels.getInputStream();
	}

	@Override
	public int[] getPixels() {
		return vncPixels.getPixels();
	}

}
