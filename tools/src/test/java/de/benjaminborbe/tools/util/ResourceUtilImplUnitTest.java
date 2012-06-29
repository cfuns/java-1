package de.benjaminborbe.tools.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ResourceUtilImplUnitTest {

	private static final String TEST_FILENAME = "test.txt";

	private static final String TEST_CONTENT = "testContent";

	@Test
	public void testGetResourceContentString() throws Exception {
		final StreamUtil streamUtil = new StreamUtil();
		final ResourceUtil resourceUtil = new ResourceUtilImpl(streamUtil);
		assertEquals(TEST_CONTENT + "\n", resourceUtil.getResourceContentString(TEST_FILENAME));
	}

	@Test
	public void testGetResourceContentByteArray() throws Exception {
		final StreamUtil streamUtil = new StreamUtil();
		final ResourceUtil resourceUtil = new ResourceUtilImpl(streamUtil);
		assertEquals(TEST_CONTENT, new String(resourceUtil.getResourceContentByteArray(TEST_FILENAME)));
	}
}
