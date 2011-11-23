package de.benjaminborbe.tools.mock;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.easymock.EasyMock;

import com.google.inject.AbstractModule;
import com.google.inject.Provider;
import com.google.inject.Singleton;

public class ServletModuleMock extends AbstractModule {

	private class HttpServletRequestMockProvider implements Provider<HttpServletRequest> {

		public HttpServletRequest get() {
			final HttpServletRequest servletRequest = EasyMock.createMock(HttpServletRequest.class);

			EasyMock.expect(servletRequest.getServerName()).andReturn("localhost").anyTimes();
			EasyMock.expect(servletRequest.getServerPort()).andReturn(80).anyTimes();
			EasyMock.expect(servletRequest.getServletPath()).andReturn("entenhausen").anyTimes();
			EasyMock.expect(servletRequest.getContextPath()).andReturn("/app").anyTimes();
			EasyMock.expect(servletRequest.getRequestURI()).andReturn("/app/ducktales/entenhausen").anyTimes();
			EasyMock.expect(servletRequest.isSecure()).andReturn(false).anyTimes();

			EasyMock.replay(servletRequest);

			return servletRequest;
		}

		@Override
		public String toString() {
			return "RequestProvider";
		}
	};

	private static final class ServletContextProvider implements Provider<ServletContext> {

		public ServletContext get() {
			return new ServletContextMock();
		}

		@Override
		public String toString() {
			return "ServletContext";
		}
	}

	@Override
	protected void configure() {
		bind(HttpSession.class).to(HttpSessionMock.class).in(Singleton.class);

		// request
		final Provider<HttpServletRequest> httpServletRequest = new HttpServletRequestMockProvider();

		bind(HttpServletRequest.class).toProvider(httpServletRequest).in(Singleton.class);
		bind(ServletRequest.class).toProvider(httpServletRequest).in(Singleton.class);

		bind(ServletContext.class).toProvider(new ServletContextProvider()).in(Singleton.class);
	}

}
