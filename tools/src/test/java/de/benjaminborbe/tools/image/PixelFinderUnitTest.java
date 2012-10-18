package de.benjaminborbe.tools.image;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Collection;

import org.easymock.EasyMock;
import org.junit.Test;
import org.slf4j.Logger;

public class PixelFinderUnitTest {

	@Test
	public void testFind() throws Exception {
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);
		final int[] rbgArray = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12 };
		final int width = 4;
		final int height = 3;
		final Pixels image = new PixelsImpl(rbgArray, width, height);
		final PixelFinder pixelFinder = new PixelFinder(logger);
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
}
