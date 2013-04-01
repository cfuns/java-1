package de.benjaminborbe.authentication.gui.util;

import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.tools.url.UrlUtilImpl;
import org.easymock.EasyMock;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;

import static org.junit.Assert.assertEquals;

public class AuthenticationGuiLinkFactoryUnitTest {

	@Test
	public void testGetEmailValidationUrl() throws Exception {
		final UrlUtil urlUtil = new UrlUtilImpl();
		final AuthenticationGuiLinkFactory authenticationGuiLinkFactory = new AuthenticationGuiLinkFactory(urlUtil);

		final HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
		EasyMock.expect(request.getScheme()).andReturn("http").anyTimes();
		EasyMock.expect(request.getServerPort()).andReturn(80).anyTimes();
		EasyMock.expect(request.getServerName()).andReturn("bb").anyTimes();
		EasyMock.expect(request.getContextPath()).andReturn("/bb").anyTimes();
		EasyMock.replay(request);
		assertEquals("http://bb/bb/authentication/email/valdation?email_verify_token=%s&user_id=%s", authenticationGuiLinkFactory.getEmailValidationUrl(request));

	}
}
