package de.benjaminborbe.vnc.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class VncPixelTest {

	@Test
	public void testRGB() {
		final VncPixel pixel = new VncPixel(0xAABBCC);
		assertEquals(0xCC, pixel.getBlue());
		assertEquals(0xBB, pixel.getGreen());
		assertEquals(0xAA, pixel.getRed());
	}
}
