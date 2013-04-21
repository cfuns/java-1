package de.benjaminborbe.tools.util;

import de.benjaminborbe.tools.stream.ChannelTools;
import de.benjaminborbe.tools.stream.StreamUtil;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ResourceUtilImplUnitTest {

	private static final String TEST_FILENAME = "test.txt";

	private static final String TEST_CONTENT = "testContent";

	@Test
	public void testGetResourceContentAsString() throws Exception {
		final StreamUtil streamUtil = new StreamUtil(new ChannelTools());
		final ResourceUtil resourceUtil = new ResourceUtilImpl(streamUtil);
		assertEquals(TEST_CONTENT + "\n", resourceUtil.getResourceContentAsString(TEST_FILENAME));
	}

	@Test
	public void testGetResourceContentAsByteArray() throws Exception {
		final StreamUtil streamUtil = new StreamUtil(new ChannelTools());
		final ResourceUtil resourceUtil = new ResourceUtilImpl(streamUtil);
		assertEquals(TEST_CONTENT, new String(resourceUtil.getResourceContentAsByteArray(TEST_FILENAME)));
	}
}
