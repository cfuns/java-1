package de.benjaminborbe.wow.xmpp;

import de.benjaminborbe.tools.image.Pixels;
import de.benjaminborbe.vnc.api.VncPixels;

public class PixelsAdapter implements Pixels {

	private final VncPixels vncPixels;

	public PixelsAdapter(final VncPixels vncPixels) {
		this.vncPixels = vncPixels;
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
	public Pixels getSubPixel(final int x, final int y, final int width, final int height) {
		return new PixelsAdapter(vncPixels.getSubPixel(x, y, width, height));
	}

	@Override
	public int getPixel(final int x, final int y) {
		return vncPixels.getPixel(x, y);
	}

	@Override
	public Pixels getCopy() {
		return new PixelsAdapter(vncPixels.getCopy());
	}

	@Override
	public void setPixel(final int x, final int y, final int rgbValue) {

	}

}
