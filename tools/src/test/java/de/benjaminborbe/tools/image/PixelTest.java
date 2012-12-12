package de.benjaminborbe.tools.image;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class PixelTest {

	@Test
	public void testRGB() {
		final Pixel pixel = new Pixel(0xAABBCC);
		assertEquals(0xCC, pixel.getBlue());
		assertEquals(0xBB, pixel.getGreen());
		assertEquals(0xAA, pixel.getRed());
	}

	@Test
	public void testIsRed() {
		final Pixel pixel = new Pixel(0x482d26);
		assertTrue(pixel.isRed());
		assertFalse(pixel.isBlue());
		assertFalse(pixel.isGreen());
	}

	@Test
	public void testIsBlue() {
		final Pixel pixel = new Pixel(0x1d3646);
		assertTrue(pixel.isBlue());
		assertFalse(pixel.isRed());
		assertFalse(pixel.isGreen());
	}

}
