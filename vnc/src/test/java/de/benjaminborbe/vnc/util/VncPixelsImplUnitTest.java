package de.benjaminborbe.vnc.util;

import static org.junit.Assert.assertEquals;

import java.io.InputStream;

import org.junit.Test;

import de.benjaminborbe.vnc.api.VncPixels;

public class VncPixelsImplUnitTest {

	@Test
	public void testPixel() throws Exception {
		final int[] data = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12 };
		final VncPixels pixels = new VncPixelsImpl(data, 4, 3);
		assertEquals(4, pixels.getWidth());
		assertEquals(3, pixels.getHeight());
		int counter = 0;
		for (int y = 0; y < pixels.getHeight(); ++y) {
			for (int x = 0; x < pixels.getWidth(); ++x) {
				assertEquals("x:" + x + " y:" + y + " = " + data[counter], data[counter], pixels.getPixel(x + 1, y + 1));
				counter++;
			}
		}
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

	@Test
	public void testInputStream() throws Exception {
		final int[] data = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12 };
		final VncPixels pixels = new VncPixelsImpl(data, 4, 3);
		int counter = 0;
		int value = 0;
		final InputStream i = pixels.getInputStream();
		while ((value = i.read()) != -1) {
			assertEquals(data[counter], value);
			counter++;
		}
		assertEquals(counter, data.length);
	}

	@Test
	public void testInputStreamSub() throws Exception {
		final int[] data = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12 };
		{
			final VncPixels pixels = new VncPixelsImpl(data, 4, 3).getSubPixel(1, 1, 2, 2);
			assertEquals(2, pixels.getWidth());
			assertEquals(2, pixels.getHeight());
			final InputStream i = pixels.getInputStream();
			assertEquals(1, i.read());
			assertEquals(2, i.read());
			assertEquals(5, i.read());
			assertEquals(6, i.read());
			assertEquals(-1, i.read());
		}
		{
			final VncPixels pixels = new VncPixelsImpl(data, 4, 3).getSubPixel(3, 2, 2, 2);
			assertEquals(2, pixels.getWidth());
			assertEquals(2, pixels.getHeight());
			final InputStream i = pixels.getInputStream();
			assertEquals(7, i.read());
			assertEquals(8, i.read());
			assertEquals(11, i.read());
			assertEquals(12, i.read());
			assertEquals(-1, i.read());
		}
	}
}
