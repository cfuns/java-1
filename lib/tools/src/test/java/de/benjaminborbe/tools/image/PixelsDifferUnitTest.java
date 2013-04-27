package de.benjaminborbe.tools.image;

import org.easymock.EasyMock;
import org.junit.Test;
import org.slf4j.Logger;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class PixelsDifferUnitTest {

	@Test
	public void testDiff() {
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);

		final PixelsDiffer vncPixelsDiffer = new PixelsDiffer(logger);
		final int[] dataA = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
		final Pixels pixelsA = new PixelsImpl(dataA, 4, 3);
		final int[] dataB = {1, 2, 3, 4, 5, 6, 7, 8, 0, 10, 11, 12};
		final Pixels pixelsB = new PixelsImpl(dataB, 4, 3);
		final List<Coordinate> result = vncPixelsDiffer.diff(pixelsA, pixelsB);
		assertNotNull(result);
		assertEquals(1, result.size());
		assertEquals(1, result.get(0).getX());
		assertEquals(3, result.get(0).getY());
	}

	@Test
	public void testDiffColorFilter() {
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);

		final PixelsDiffer vncPixelsDiffer = new PixelsDiffer(logger);
		final int[] dataA = {0xAA1234, 0xBB1234, 0xCC1234, 0xDD1234, 0xFF1234, 0x001234, 0x111234, 0x221234, 0xAA0000, 0x331234, 0x441234, 0x551234};
		final Pixels pixelsA = new PixelsImpl(dataA, 4, 3);
		final int[] dataB = {0xAA2345, 0xBB2345, 0xCC2345, 0xDD2345, 0xFF2345, 0x002345, 0x112345, 0x222345, 0xFF0000, 0x332345, 0x442345, 0x552345};
		final Pixels pixelsB = new PixelsImpl(dataB, 4, 3);
		final List<Coordinate> result = vncPixelsDiffer.diff(pixelsA, pixelsB, new PixelFilterMask(0xFF0000));
		assertNotNull(result);
		assertEquals(1, result.size());
		assertEquals(1, result.get(0).getX());
		assertEquals(3, result.get(0).getY());
	}
}
