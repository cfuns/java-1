package de.benjaminborbe.tools.image;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Collection;

import javax.imageio.ImageIO;

import org.easymock.EasyMock;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;

public class PixelFinderUnitTest {

	@Test
	public void testFind() throws Exception {
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);
		final PixelFinder pixelFinder = new PixelFinder(logger);

		final int[] rbgArray = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12 };
		final int width = 4;
		final int height = 3;
		final Pixels image = new PixelsImpl(rbgArray, width, height);
		{
			final Pixels subImage = image.getSubPixel(1, 1, 2, 2);
			assertEquals(image.getPixel(1, 1), subImage.getPixel(1, 1));
			assertEquals(2, subImage.getWidth());
			assertEquals(2, subImage.getHeight());
			final Collection<Coordinate> cs = pixelFinder.find(image, subImage);
			assertNotNull(cs);
			assertEquals(1, cs.size());
		}
		for (int x = 1; x <= width; ++x) {
			for (int y = 1; y <= height; ++y) {
				final Pixels subImage = image.getSubPixel(x, y, 1, 1);
				assertEquals(image.getPixel(x, y), subImage.getPixel(1, 1));
				assertEquals(1, subImage.getWidth());
				assertEquals(1, subImage.getHeight());
				final Collection<Coordinate> cs = pixelFinder.find(image, subImage);
				assertNotNull(cs);
				assertEquals("x:" + x + " y: " + y, 1, cs.size());
			}
		}
	}

	@Test
	public void testGetMatchInPercent() {
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);
		final PixelFinder pixelFinder = new PixelFinder(logger);

		assertEquals(100, pixelFinder.getMatchInPercent(0xFF, 0xFF));
		assertEquals(0, pixelFinder.getMatchInPercent(0x0, 0xFF));
		assertEquals(0, pixelFinder.getMatchInPercent(0xFF, 0x0));
	}

	@Test
	public void testMatch() {
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);
		final PixelFinder pixelFinder = new PixelFinder(logger);

		assertTrue(pixelFinder.match(0xFF, 0xFF, 100));
		assertTrue(pixelFinder.match(0xFF, 0x00, 0));
		assertFalse(pixelFinder.match(0xFF, 0xEE, 100));
	}

	@Ignore
	@Test
	public void testFoo() throws Exception {
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);
		final PixelFinder pixelFinder = new PixelFinder(logger);

		final BufferedImage image = ImageIO.read(new File("/tmp/foo.bmp"));
		final BufferedImage subimage = ImageIO.read(new File("/tmp/wow-app-icon.bmp"));
		final Collection<Coordinate> cs = pixelFinder.find(new PixelsImpl(image), new PixelsImpl(subimage), 70);
		assertNotNull(cs);
		assertEquals(1, cs.size());
	}
}
