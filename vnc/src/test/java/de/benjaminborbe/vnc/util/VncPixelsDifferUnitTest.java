package de.benjaminborbe.vnc.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

import javax.imageio.ImageIO;

import org.easymock.EasyMock;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;

import de.benjaminborbe.vnc.api.VncLocation;
import de.benjaminborbe.vnc.api.VncPixels;

public class VncPixelsDifferUnitTest {

	@Ignore
	@Test
	public void testDiff() {
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);

		final VncPixelsDiffer vncPixelsDiffer = new VncPixelsDiffer(logger);
		final int[] dataA = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12 };
		final VncPixels pixelsA = new VncPixelsImpl(dataA, 4, 3);
		final int[] dataB = { 1, 2, 3, 4, 5, 6, 7, 8, 0, 10, 11, 12 };
		final VncPixels pixelsB = new VncPixelsImpl(dataB, 4, 3);
		final List<VncLocation> result = vncPixelsDiffer.diff(pixelsA, pixelsB);
		assertNotNull(result);
		assertEquals(1, result.size());
		assertEquals(1, result.get(0).getX());
		assertEquals(3, result.get(0).getY());
	}

	@Ignore
	@Test
	public void testDiffColorFilter() {
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);

		final VncPixelsDiffer vncPixelsDiffer = new VncPixelsDiffer(logger);
		final int[] dataA = { 0xAA1234, 0xBB1234, 0xCC1234, 0xDD1234, 0xFF1234, 0x001234, 0x111234, 0x221234, 0xAA0000, 0x331234, 0x441234, 0x551234 };
		final VncPixels pixelsA = new VncPixelsImpl(dataA, 4, 3);
		final int[] dataB = { 0xAA2345, 0xBB2345, 0xCC2345, 0xDD2345, 0xFF2345, 0x002345, 0x112345, 0x222345, 0xFF0000, 0x332345, 0x442345, 0x552345 };
		final VncPixels pixelsB = new VncPixelsImpl(dataB, 4, 3);
		final List<VncLocation> result = vncPixelsDiffer.diff(pixelsA, pixelsB, 0xFF0000, 0);
		assertNotNull(result);
		assertEquals(1, result.size());
		assertEquals(1, result.get(0).getX());
		assertEquals(3, result.get(0).getY());
	}

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
		final VncPixels afterPixel = new VncPixelsImpl(after);
		final VncPixels beforePixel = new VncPixelsImpl(before);
		assertEquals(1920, beforePixel.getWidth());
		assertEquals(1200, beforePixel.getHeight());
		assertEquals(1920, afterPixel.getWidth());
		assertEquals(1200, afterPixel.getHeight());

		// assertEquals(0x0, before.getRGB(0, 0));

		final VncPixelsDiffer vncPixelsDiffer = new VncPixelsDiffer(logger);
		final List<VncLocation> result = vncPixelsDiffer.diff(beforePixel, afterPixel, 0x00FFFFFF, 0);
		for (final VncLocation r : result) {
			// if (afterPixel.getPixel(r.getX(), r.getY()) > 0x400000 &&
			// afterPixel.getPixel(r.getX(), r.getY()) < 0x800000) {
			System.err.println(r);
			// }
		}
		assertEquals(1, result.size());
	}
}
