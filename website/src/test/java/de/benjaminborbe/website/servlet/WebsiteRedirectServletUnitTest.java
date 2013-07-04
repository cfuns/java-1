package de.benjaminborbe.website.servlet;

import org.easymock.EasyMock;
import org.junit.Test;
import org.slf4j.Logger;

import javax.servlet.http.HttpServletRequest;

import static org.junit.Assert.assertEquals;

public class WebsiteRedirectServletUnitTest {

	@Test
	public void testbuildRedirectTargetPath() {
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);

		final WebsiteRedirectServlet calendarServlet = new WebsiteRedirectServlet(logger, null, null, null, null, null, null) {

			private static final long serialVersionUID = 1L;

			@Override
			protected String getTarget() {
				return "test";
			}
		};
		final HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
		EasyMock.expect(request.getContextPath()).andReturn("/bb");
		EasyMock.replay(request);
		assertEquals("/bb/test", calendarServlet.buildRedirectTargetPath(request));
	}
}
