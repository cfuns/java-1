package de.benjaminborbe.slash.gui.servlet;

import static org.junit.Assert.assertEquals;

import javax.servlet.http.HttpServletRequest;

import org.easymock.EasyMock;
import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.slash.gui.guice.SlashGuiModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class SlashGuiServletTest {

	@Test
	public void testSingleton() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new SlashGuiModulesMock());
		final SlashGuiServlet a = injector.getInstance(SlashGuiServlet.class);
		final SlashGuiServlet b = injector.getInstance(SlashGuiServlet.class);
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		assertEquals(a, b);
	}

	@Test
	public void testbuildRedirectTargetPath() {
		final SlashGuiServlet slashServlet = new SlashGuiServlet(null);
		final HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
		EasyMock.expect(request.getContextPath()).andReturn("/bb");
		EasyMock.replay(request);
		assertEquals("/bb/dashboard", slashServlet.buildRedirectTargetPath(request));
	}
}
