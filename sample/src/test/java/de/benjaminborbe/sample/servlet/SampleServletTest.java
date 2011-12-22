package de.benjaminborbe.sample.servlet;

import static org.junit.Assert.assertEquals;

import javax.servlet.http.HttpServletRequest;

import org.easymock.EasyMock;
import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.sample.guice.SampleModulesMock;
import de.benjaminborbe.sample.servlet.SampleServlet;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class SampleServletTest {

	@Test
	public void Singleton() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new SampleModulesMock());
		final SampleServlet a = injector.getInstance(SampleServlet.class);
		final SampleServlet b = injector.getInstance(SampleServlet.class);
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		assertEquals(a, b);
	}

	@Test
	public void buildRedirectTargetPath() {
		final SampleServlet sampleServlet = new SampleServlet(null);
		final HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
		EasyMock.expect(request.getContextPath()).andReturn("/bb");
		EasyMock.replay(request);
		assertEquals("/bb/dashboard", sampleServlet.buildRedirectTargetPath(request));
	}
}
