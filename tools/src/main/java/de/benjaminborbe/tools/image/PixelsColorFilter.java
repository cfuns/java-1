package de.benjaminborbe.tools.image;

public class PixelsColorFilter extends PixelsAdapter implements Pixels {

	private final int mask;

	public PixelsColorFilter(final Pixels vncPixels, final int mask) {
		super(vncPixels);
		this.mask = mask;
	}

	@Override
	public int getPixel(final int x, final int y) {
		return super.getPixel(x, y) & mask;
	}

}
