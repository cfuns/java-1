package de.benjaminborbe.vnc.util;

import java.awt.image.BufferedImage;
import de.benjaminborbe.vnc.api.VncPixels;

public class VncPixelsImpl implements VncPixels {

	private final BufferedImage bufferedImage;

	private final int x;

	private final int y;

	private final int height;

	private final int width;

	private final int orgWidth;

	public VncPixelsImpl(final BufferedImage bufferedImage) {
		this(bufferedImage, 1, 1, bufferedImage.getWidth(), bufferedImage.getHeight(), bufferedImage.getWidth());
	}

	private VncPixelsImpl(final BufferedImage bufferedImage, final int x, final int y, final int width, final int height, final int orgWidth) {
		this.bufferedImage = bufferedImage;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.orgWidth = orgWidth;
	}

	public VncPixelsImpl(final int[] pixels, final int width, final int height) {
		this(buildBufferedImage(pixels, width, height));
	}

	private static BufferedImage buildBufferedImage(final int[] pixels, final int width, final int height) {
		// final ColorModel colorModel = new DirectColorModel(24, 0xff0000, 0xff00, 0xff);
		// final SampleModel sampleModel = colorModel.createCompatibleSampleModel(width,
		// height);
		// final DataBuffer dataBuffer = new DataBufferInt(pixels, width * height);
		// final WritableRaster raster = Raster.createWritableRaster(sampleModel, dataBuffer,
		// null);
		// final BufferedImage bi = new BufferedImage(colorModel, raster, false, null);

		final BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		for (int x = 0; x < width; ++x) {
			for (int y = 0; y < height; ++y) {
				bi.setRGB(x, y, pixels[x + y * width]);
			}
		}
		// bi.setRGB(0, 0, width, height, pixels, 0, 0);
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
	public VncPixels getSubPixel(final int x, final int y, final int width, final int height) {
		if (x > this.width) {
			throw new IllegalArgumentException("out of range");
		}
		if (y > this.height) {
			throw new IllegalArgumentException("out of range");
		}
		return new VncPixelsImpl(bufferedImage, this.x + x - 1, this.y + y - 1, width, height, orgWidth);
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

}
