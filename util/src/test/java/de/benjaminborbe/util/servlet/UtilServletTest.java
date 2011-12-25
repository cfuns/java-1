package de.benjaminborbe.util.servlet;

import static org.junit.Assert.assertEquals;

import javax.servlet.http.HttpServletRequest;

import org.easymock.EasyMock;
import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.util.guice.UtilModulesMock;
import de.benjaminborbe.util.servlet.UtilServlet;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class UtilServletTest {

	@Test
	public void Singleton() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new UtilModulesMock());
		final UtilServlet a = injector.getInstance(UtilServlet.class);
		final UtilServlet b = injector.getInstance(UtilServlet.class);
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		assertEquals(a, b);
	}

	@Test
	public void buildRedirectTargetPath() {
		final UtilServlet utilServlet = new UtilServlet(null);
		final HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
		EasyMock.expect(request.getContextPath()).andReturn("/bb");
		EasyMock.replay(request);
		assertEquals("/bb/dashboard", utilServlet.buildRedirectTargetPath(request));
	}
}
