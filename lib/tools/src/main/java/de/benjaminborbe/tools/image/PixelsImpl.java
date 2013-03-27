package de.benjaminborbe.tools.image;

import java.awt.image.BufferedImage;

public class PixelsImpl implements Pixels {

	private final BufferedImage bufferedImage;

	private final int x;

	private final int y;

	private final int height;

	private final int width;

	private final int orgWidth;

	private PixelsImpl(final BufferedImage bufferedImage, final int x, final int y, final int width, final int height, final int orgWidth) {
		this.bufferedImage = bufferedImage;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.orgWidth = orgWidth;
	}

	public PixelsImpl(final BufferedImage bufferedImage) {
		this(bufferedImage, 1, 1, bufferedImage.getWidth(), bufferedImage.getHeight(), bufferedImage.getWidth());
	}

	public PixelsImpl(final int[] pixels, final int width, final int height) {
		this(buildBufferedImage(pixels, width, height));
	}

	private static BufferedImage buildBufferedImage(final int[] pixels, final int width, final int height) {
		final BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		for (int x = 0; x < width; ++x) {
			for (int y = 0; y < height; ++y) {
				bi.setRGB(x, y, pixels[x + y * width]);
			}
		}
		return bi;
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
	public Pixels getSubPixel(final int x, final int y, final int width, final int height) {
		if (x > this.width) {
			throw new IllegalArgumentException("out of range");
		}
		if (y > this.height) {
			throw new IllegalArgumentException("out of range");
		}
		return new PixelsImpl(bufferedImage, this.x + x - 1, this.y + y - 1, width, height, orgWidth);
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
		return bufferedImage.getRGB(xn - 1, yn - 1);
	}

	public BufferedImage getBufferedImage() {
		return bufferedImage;
	}

	@Override
	public Pixels getCopy() {
		final int[] rgbArray = new int[bufferedImage.getWidth() * bufferedImage.getHeight()];
		for (int x = 0; x < width; ++x) {
			for (int y = 0; y < height; ++y) {
				// rgbArray[x + y * bufferedImage.getWidth()] = bufferedImage.getRGB(x, y);
				rgbArray[x + y * bufferedImage.getWidth()] = getPixel(x + 1, y + 1);
			}
		}
		return new PixelsImpl(buildBufferedImage(rgbArray, bufferedImage.getWidth(), bufferedImage.getHeight()), x, y, width, height, orgWidth);
	}

	@Override
	public void setPixel(final int x, final int y, final int rgbValue) {
		final int xn = x + this.x - 1;
		final int yn = y + this.y - 1;
		bufferedImage.setRGB(xn - 1, yn - 1, rgbValue);
	}
}
