package de.benjaminborbe.tools.stream;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class InputStreamCopyUnitTest {

	@Test
	public void testStreamRead() throws Exception {
		final byte[] content = "test".getBytes();
		final InputStream inputStream = new ByteArrayInputStream(content);
		final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		final StreamUtil streamUtil = new StreamUtil(new ChannelTools());
		streamUtil.copy(inputStream, outputStream);
		assertThat(outputStream.toByteArray(), is(content));
	}

	@Test
	public void testCopy() throws Exception {
		final byte[] content = "test".getBytes();
		final ByteArrayOutputStream copyOutputStream = new ByteArrayOutputStream();
		final InputStream inputStream = new InputStreamCopy(new ByteArrayInputStream(content), copyOutputStream);
		final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		final StreamUtil streamUtil = new StreamUtil(new ChannelTools());
		streamUtil.copy(inputStream, outputStream);
		assertThat(outputStream.toByteArray(), is(content));
		assertThat(copyOutputStream.toByteArray(), is(content));
	}
}
