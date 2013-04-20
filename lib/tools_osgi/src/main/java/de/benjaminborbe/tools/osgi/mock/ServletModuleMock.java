package de.benjaminborbe.tools.osgi.mock;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.google.inject.AbstractModule;
import com.google.inject.Provider;
import javax.inject.Singleton;

public class ServletModuleMock extends AbstractModule {

	private class HttpServletRequestMockProvider implements Provider<HttpServletRequest> {

		@Override
		public HttpServletRequest get() {
			final HttpServletRequest servletRequest = new HttpServletRequest() {

				@Override
				public Object getAttribute(final String name) {
					return null;
				}

				@SuppressWarnings("rawtypes")
				@Override
				public Enumeration getAttributeNames() {
					return null;
				}

				@Override
				public String getCharacterEncoding() {
					return null;
				}

				@Override
				public void setCharacterEncoding(final String env) throws UnsupportedEncodingException {
				}

				@Override
				public int getContentLength() {
					return 0;
				}

				@Override
				public String getContentType() {
					return null;
				}

				@Override
				public ServletInputStream getInputStream() throws IOException {
					return null;
				}

				@Override
				public String getParameter(final String name) {
					return null;
				}

				@SuppressWarnings("rawtypes")
				@Override
				public Enumeration getParameterNames() {
					return null;
				}

				@Override
				public String[] getParameterValues(final String name) {
					return null;
				}

				@SuppressWarnings("rawtypes")
				@Override
				public Map getParameterMap() {
					return null;
				}

				@Override
				public String getProtocol() {
					return null;
				}

				@Override
				public String getScheme() {
					return null;
				}

				@Override
				public String getServerName() {
					return "localhost";
				}

				@Override
				public int getServerPort() {
					return 80;
				}

				@Override
				public BufferedReader getReader() throws IOException {
					return null;
				}

				@Override
				public String getRemoteAddr() {
					return null;
				}

				@Override
				public String getRemoteHost() {
					return null;
				}

				@Override
				public void setAttribute(final String name, final Object o) {
				}

				@Override
				public void removeAttribute(final String name) {
				}

				@Override
				public Locale getLocale() {
					return null;
				}

				@SuppressWarnings("rawtypes")
				@Override
				public Enumeration getLocales() {
					return null;
				}

				@Override
				public boolean isSecure() {
					return false;
				}

				@Override
				public RequestDispatcher getRequestDispatcher(final String path) {
					return null;
				}

				@Override
				public String getRealPath(final String path) {
					return null;
				}

				@Override
				public int getRemotePort() {
					return 0;
				}

				@Override
				public String getLocalName() {
					return null;
				}

				@Override
				public String getLocalAddr() {
					return null;
				}

				@Override
				public int getLocalPort() {
					return 0;
				}

				@Override
				public String getAuthType() {
					return null;
				}

				@Override
				public Cookie[] getCookies() {
					return null;
				}

				@Override
				public long getDateHeader(final String name) {
					return 0;
				}

				@Override
				public String getHeader(final String name) {
					return null;
				}

				@SuppressWarnings("rawtypes")
				@Override
				public Enumeration getHeaders(final String name) {
					return null;
				}

				@SuppressWarnings("rawtypes")
				@Override
				public Enumeration getHeaderNames() {
					return null;
				}

				@Override
				public int getIntHeader(final String name) {
					return 0;
				}

				@Override
				public String getMethod() {
					return null;
				}

				@Override
				public String getPathInfo() {
					return null;
				}

				@Override
				public String getPathTranslated() {
					return null;
				}

				@Override
				public String getContextPath() {
					return "/app";
				}

				@Override
				public String getQueryString() {
					return null;
				}

				@Override
				public String getRemoteUser() {
					return null;
				}

				@Override
				public boolean isUserInRole(final String role) {
					return false;
				}

				@Override
				public Principal getUserPrincipal() {
					return null;
				}

				@Override
				public String getRequestedSessionId() {
					return null;
				}

				@Override
				public String getRequestURI() {
					return "/app/ducktales/entenhausen";
				}

				@Override
				public StringBuffer getRequestURL() {
					return null;
				}

				@Override
				public String getServletPath() {
					return "entenhausen";
				}

				@Override
				public HttpSession getSession(final boolean create) {
					return null;
				}

				@Override
				public HttpSession getSession() {
					return null;
				}

				@Override
				public boolean isRequestedSessionIdValid() {
					return false;
				}

				@Override
				public boolean isRequestedSessionIdFromCookie() {
					return false;
				}

				@Override
				public boolean isRequestedSessionIdFromURL() {
					return false;
				}

				@Override
				public boolean isRequestedSessionIdFromUrl() {
					return false;
				}
			};

			return servletRequest;
		}

		@Override
		public String toString() {
			return "RequestProvider";
		}
	};

	private static final class ServletContextProvider implements Provider<ServletContext> {

		@Override
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
