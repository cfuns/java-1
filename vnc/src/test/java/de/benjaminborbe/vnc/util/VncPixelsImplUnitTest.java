package de.benjaminborbe.vnc.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import de.benjaminborbe.vnc.api.VncPixels;

public class VncPixelsImplUnitTest {

	@Test
	public void testPixel() throws Exception {
		{
			final int[] data = { 0xFFFFFF };
			final VncPixelsImpl pixels = new VncPixelsImpl(data, 1, 1);
			assertEquals(1, pixels.getWidth());
			assertEquals(1, pixels.getHeight());

			assertEquals(0xFFFFFF, pixels.getBufferedImage().getRGB(0, 0));
			assertEquals(0xFFFFFF, pixels.getPixel(1, 1));
		}
		{
			final int[] data = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12 };
			final VncPixelsImpl pixels = new VncPixelsImpl(data, 4, 3);
			assertEquals(4, pixels.getWidth());
			assertEquals(3, pixels.getHeight());
			int counter = 0;
			for (int y = 0; y < pixels.getHeight(); ++y) {
				for (int x = 0; x < pixels.getWidth(); ++x) {
					assertEquals("x:" + x + " y:" + y + " = " + data[counter], data[counter], pixels.getBufferedImage().getRGB(x, y));
					assertEquals("x:" + x + " y:" + y + " = " + data[counter], data[counter], pixels.getPixel(x + 1, y + 1));
					counter++;
				}
			}
		}
	}

	@Test
	public void testCopy() {
		final int[] data = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12 };
		final VncPixelsImpl pixels = new VncPixelsImpl(data, 4, 3);
		final VncPixels copy = pixels.getCopy();
		assertEquals(copy.getWidth(), pixels.getWidth());
		assertEquals(copy.getHeight(), pixels.getHeight());
		assertEquals(1, pixels.getPixel(1, 1));
		pixels.getBufferedImage().setRGB(0, 0, 0);
		assertEquals(0, pixels.getPixel(1, 1));
		assertEquals(1, copy.getPixel(1, 1));
	}

	@Test
	public void testSubPixel() throws Exception {
		final int[] data = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12 };
		{
			final VncPixels pixels = new VncPixelsImpl(data, 4, 3).getSubPixel(1, 1, 2, 2);
			assertEquals(2, pixels.getWidth());
			assertEquals(2, pixels.getHeight());
			assertEquals(1, pixels.getPixel(1, 1));
			assertEquals(2, pixels.getPixel(2, 1));
			assertEquals(5, pixels.getPixel(1, 2));
			assertEquals(6, pixels.getPixel(2, 2));
		}
		{
			final VncPixels pixels = new VncPixelsImpl(data, 4, 3).getSubPixel(3, 2, 2, 2);
			assertEquals(2, pixels.getWidth());
			assertEquals(2, pixels.getHeight());
			assertEquals(7, pixels.getPixel(1, 1));
			assertEquals(8, pixels.getPixel(2, 1));
			assertEquals(11, pixels.getPixel(1, 2));
			assertEquals(12, pixels.getPixel(2, 2));
		}
	}

}
