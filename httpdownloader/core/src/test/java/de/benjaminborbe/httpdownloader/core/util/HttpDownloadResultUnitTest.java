package de.benjaminborbe.httpdownloader.core.util;

import de.benjaminborbe.tools.util.Encoding;
import org.junit.Test;

import java.net.URL;
import java.util.HashMap;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
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
		final int responseCode = 200;
		final HttpDownloadResult result = new HttpDownloadResultImpl(url, duration, content, contentType, contentEncoding, new HashMap<String, List<String>>(), responseCode);
		assertEquals(duration, result.getDuration());
		assertEquals(contentEncoding, result.getContentEncoding());
		assertEquals(contentEncoding.getEncoding(), result.getContentEncoding().getEncoding());
		assertEquals(content.length, result.getContent().length);
		assertThat(result.getResponseCode(), is(responseCode));
		assertTrue(content.hashCode() != result.getContent().hashCode());
		for (int i = 0; i < content.length; ++i) {
			assertEquals(content[i], result.getContent()[i]);
		}
	}
}
