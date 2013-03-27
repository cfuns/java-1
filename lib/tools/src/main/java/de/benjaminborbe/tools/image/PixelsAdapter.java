package de.benjaminborbe.tools.image;

public class PixelsAdapter implements Pixels {

	private final Pixels vncPixels;

	public PixelsAdapter(final Pixels vncPixels) {
		this.vncPixels = vncPixels;
	}

	@Override
	public Pixels getSubPixel(final int xstart, final int ystart, final int xend, final int yend) {
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
	public Pixels getCopy() {
		return vncPixels.getCopy();
	}

	@Override
	public void setPixel(final int x, final int y, final int rgbValue) {
		vncPixels.setPixel(x, y, rgbValue);
	}

}
