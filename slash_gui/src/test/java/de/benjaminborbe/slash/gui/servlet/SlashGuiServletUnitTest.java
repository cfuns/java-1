package de.benjaminborbe.slash.gui.servlet;

import static org.junit.Assert.assertEquals;

import javax.servlet.http.HttpServletRequest;

import org.easymock.EasyMock;
import org.junit.Test;

public class SlashGuiServletUnitTest {

	@Test
	public void testbuildRedirectTargetPath() {
		final SlashGuiServlet slashServlet = new SlashGuiServlet(null);
		final HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
		EasyMock.expect(request.getContextPath()).andReturn("/bb");
		EasyMock.replay(request);
		assertEquals("/bb/dashboard", slashServlet.buildRedirectTargetPath(request));
	}
}
