package de.benjaminborbe.websearch.core.util;

import de.benjaminborbe.httpdownloader.api.HttpContent;
import de.benjaminborbe.tools.mapper.MapperByteArray;
import de.benjaminborbe.tools.util.Base64Util;
import de.benjaminborbe.tools.util.Base64UtilImpl;
import org.easymock.EasyMock;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class MapperHttpContentUnitTest {

	@Test
	public void testToString() throws Exception {
		final Base64Util base64Util = new Base64UtilImpl();
		final MapperByteArray mapperByteArray = new MapperByteArray(base64Util);
		final MapperHttpContent mapper = new MapperHttpContent(mapperByteArray);

		final byte[] content = "Hello World".getBytes();
		final HttpContent httpContent = EasyMock.createMock(HttpContent.class);
		EasyMock.expect(httpContent.getContent()).andReturn(content);
		EasyMock.replay(httpContent);

		final String string = mapper.toString(httpContent);
		assertThat(string, is(notNullValue()));
		assertThat(string, is("SGVsbG8gV29ybGQ="));
	}

	@Test
	public void testFromString() throws Exception {
		final Base64Util base64Util = new Base64UtilImpl();
		final MapperByteArray mapperByteArray = new MapperByteArray(base64Util);
		final MapperHttpContent mapper = new MapperHttpContent(mapperByteArray);
		final byte[] content = "Hello World".getBytes();
		final HttpContent httpContent = mapper.fromString("SGVsbG8gV29ybGQ=");
		assertThat(httpContent, is(notNullValue()));
		assertThat(httpContent.getContent(), is(content));

	}
}
