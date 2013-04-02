package de.benjaminborbe.proxy.gui.util;

import org.easymock.EasyMock;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ProxyUnitTest {

	@Test
	public void testProxy() throws Exception {
		Proxy proxy = new Proxy();
		final HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
		EasyMock.replay(request);
		final HttpServletResponse response = EasyMock.createMock(HttpServletResponse.class);
		EasyMock.replay(response);
		proxy.service(request, response);

	}
}
