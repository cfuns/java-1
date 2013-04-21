package de.benjaminborbe.tools.http;

import de.benjaminborbe.tools.util.Encoding;
import org.junit.Test;

import java.net.URL;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class HttpDownloadResultUnitTest {

	@Test
	public void testContent() throws Exception {
		final long duration = 1337l;
		final byte[] content = new byte[42];
		final Encoding contentEncoding = new Encoding("foo/bar");
		final String contentType = "text/html";
		final URL url = new URL("http://example.com");
		final HttpDownloadResult result = new HttpDownloadResultImpl(url, duration, content, contentType, contentEncoding, new HashMap<String, List<String>>());
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
