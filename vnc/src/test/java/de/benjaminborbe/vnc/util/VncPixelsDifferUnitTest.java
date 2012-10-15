package de.benjaminborbe.vnc.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import de.benjaminborbe.vnc.api.VncLocation;
import de.benjaminborbe.vnc.api.VncPixels;

public class VncPixelsDifferUnitTest {

	@Ignore
	@Test
	public void testDiff() {
		final VncPixelsDiffer vncPixelsDiffer = new VncPixelsDiffer();
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

	// public void testFoo() throws IOException {
	// final File a = new File("/tmp/a.dump");
	// final File b = new File("/tmp/b.dump");
	// final FileUtil fileUtil = new FileUtil(new StreamUtil());
	// final byte[] byteA = fileUtil.fileAsByteArray(a);
	// final byte[] byteB = fileUtil.fileAsByteArray(b);
	// final VncPixels pixelsA = new VncPixelsImpl(byteA, 4, 3);
	// final VncPixels pixelsB = new VncPixelsImpl(byteB, 4, 3);
	// }
}
