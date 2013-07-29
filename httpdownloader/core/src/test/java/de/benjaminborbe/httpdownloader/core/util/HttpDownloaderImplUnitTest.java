package de.benjaminborbe.httpdownloader.core.util;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class HttpDownloaderImplUnitTest {

	@Test
	public void testbuildCookieString() {
		final Map<String, String> data = new HashMap<String, String>();
		data.put("userId", "igbrown");
		data.put("sessionId", "SID77689211949");
		data.put("isAuthenticated", "true");
		final HttpDownloaderImpl httpDownloaderImpl = new HttpDownloaderImpl(null, null, null, null);
		assertEquals("isAuthenticated=true; sessionId=SID77689211949; userId=igbrown", httpDownloaderImpl.buildCookieString(data));
	}
}
