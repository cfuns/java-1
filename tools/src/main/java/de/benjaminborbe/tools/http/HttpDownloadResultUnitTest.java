package de.benjaminborbe.tools.http;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import de.benjaminborbe.tools.util.Encoding;

public class HttpDownloadResultUnitTest {

	@Test
	public void testContent() {
		final long duration = 1337l;
		final byte[] content = new byte[42];
		final Encoding contentEncoding = new Encoding("foo/bar");
		final String contentType = "text/html";
		final HttpDownloadResult result = new HttpDownloadResult(duration, content, contentType, contentEncoding);
		assertEquals(duration, result.getDuration());
		assertEquals(contentEncoding, result.getContentEncoding());
		assertEquals(contentEncoding.getEncoding(), result.getContentEncoding().getEncoding());
		assertEquals(content.length, result.getContent().length);
		assertTrue(content.hashCode() != result.getContent().hashCode());
		for (int i = 0; i < content.length; ++i) {
			assertEquals(content[i], result.getContent()[i]);
		}
	}
}
