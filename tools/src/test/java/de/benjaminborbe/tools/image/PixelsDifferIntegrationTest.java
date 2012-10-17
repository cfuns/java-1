package de.benjaminborbe.tools.image;

import static org.junit.Assert.assertEquals;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.easymock.EasyMock;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;

public class PixelsDifferIntegrationTest {

	@Ignore
	@Test
	public void testDiffBmp() throws Exception {
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);

		final BufferedImage before = ImageIO.read(new File("/tmp/before.bmp"));
		final BufferedImage after = ImageIO.read(new File("/tmp/after.bmp"));
		assertEquals(1920, before.getWidth());
		assertEquals(1200, before.getHeight());
		assertEquals(1920, after.getWidth());
		assertEquals(1200, after.getHeight());
		final Pixels afterPixel = new PixelsImpl(after);
		final Pixels beforePixel = new PixelsImpl(before);
		assertEquals(1920, beforePixel.getWidth());
		assertEquals(1200, beforePixel.getHeight());
		assertEquals(1920, afterPixel.getWidth());
		assertEquals(1200, afterPixel.getHeight());
		final int width = before.getWidth();
		final int height = before.getHeight();

		final BufferedImage diffBlueToRed = new BufferedImage(width, height, before.getType());

		for (int x = 0; x < width; ++x) {
			for (int y = 0; y < height; ++y) {
				diffBlueToRed.setRGB(x, y, getBlueToRed(before.getRGB(x, y), after.getRGB(x, y)));
			}
		}
		saveBmp(diffBlueToRed, "diffBlueToRed");

		final int range = 2;
		final int min = 20;

		final BufferedImage diffNeighbors = new BufferedImage(width, height, before.getType());
		for (int x = 0; x < width; ++x) {
			for (int y = 0; y < height; ++y) {
				final int pixel = diffBlueToRed.getRGB(x, y);
				if (pixel == 0) {
					diffNeighbors.setRGB(x, y, 0x0);
				}
				else {
					final int minX = Math.max(0, x - range);
					final int minY = Math.max(0, y - range);
					final int maxX = Math.min(width - 1, x + range);
					final int maxY = Math.min(height - 1, y + range);
					int counter = 0;
					for (int xc = minX; xc <= maxX; ++xc) {
						for (int yc = minY; yc <= maxY; ++yc) {
							counter += diffBlueToRed.getRGB(xc, yc) & 0x1;
						}
					}
					if (counter >= min) {
						diffNeighbors.setRGB(x, y, 0xFFFFFF);
					}
					else {
						diffNeighbors.setRGB(x, y, 0x0);
					}
				}
			}
		}

		saveBmp(diffNeighbors, "diffNeighbors");
	}

	private void saveBmp(final BufferedImage bufferedImage, final String filename) throws IOException {
		final BMPUtil bmpUtil = new BMPUtil();
		final File out = new File("/tmp/out/" + filename + ".bmp");
		final FileOutputStream fo = new FileOutputStream(out);
		bmpUtil.writeBMP(fo, bufferedImage, 96);
		fo.close();
	}

	private int getBlueToRed(final int b, final int a) {
		final Pixel pb = new Pixel(b);
		final Pixel pa = new Pixel(a);
		if (pb.isBlue() && pa.isRed()) {
			return 0xffffff;
		}
		else {
			return 0;
		}
		// return toGrey(a ^ b);
	}

	private int toGrey(final int pixel) {
		final int blue = pixel & 0xFF;
		final int green = pixel >> 8 & 0xFF;
		final int red = pixel >> 16 & 0xFF;
		final int grey = (blue + green + red) / 3;
		return grey | grey << 8 | grey << 16;
	}
}
