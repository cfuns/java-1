package de.benjaminborbe.proxy.gui.servlet;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.tools.util.StreamUtil;
import de.benjaminborbe.website.servlet.WebsiteServlet;
import org.slf4j.Logger;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

@Singleton
public class ProxyGuiServlet extends WebsiteServlet {

	public static final int TIMEOUT = 5000;

	private static final long serialVersionUID = 1328676176772634649L;

	private final Logger logger;

	private final StreamUtil streamUtil;

	@Inject
	public ProxyGuiServlet(final Logger logger, final UrlUtil urlUtil, final AuthenticationService authenticationService, final AuthorizationService authorizationService, final CalendarUtil calendarUtil, final TimeZoneUtil timeZoneUtil, final Provider<HttpContext> httpContextProvider, final StreamUtil streamUtil) {
		super(logger, urlUtil, authenticationService, authorizationService, calendarUtil, timeZoneUtil, httpContextProvider);
		this.logger = logger;
		this.streamUtil = streamUtil;
	}

	@Override
	protected void doService(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws ServletException, IOException, PermissionDeniedException, LoginRequiredException {
		final TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManagerAllowAll()};

		final SSLSocketFactory orgSSLSocketFactory = HttpsURLConnection.getDefaultSSLSocketFactory();
		final HostnameVerifier orgHostnameVerifier = HttpsURLConnection.getDefaultHostnameVerifier();
		try {
			final SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			final HostnameVerifier hv = new HostnameVerifierAllowAll();
			HttpsURLConnection.setDefaultHostnameVerifier(hv);
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

			proxy(request, response);
		} catch (NoSuchAlgorithmException | KeyManagementException e) {
			logger.warn(e.getClass().getName(), e);
		} finally {
			HttpsURLConnection.setDefaultHostnameVerifier(orgHostnameVerifier);
			HttpsURLConnection.setDefaultSSLSocketFactory(orgSSLSocketFactory);
		}
	}

	private void proxy(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
		InputStream inputStream = null;
		ServletOutputStream outputStream = null;
		try {
			final String servletPath = request.getContextPath() + request.getServletPath();
			logger.debug("request: " + request.getRequestURI());
			logger.debug("servlet: " + servletPath);

			final int pos = request.getRequestURI().indexOf(servletPath);
			final String uri = request.getRequestURI().substring(servletPath.length() + pos);
			logger.debug("uri: " + uri);

			final String baseUrl = "http://www.heise.de";
			final URL url = new URL(baseUrl + uri);
			final URLConnection connection = url.openConnection();
			connection.setReadTimeout(TIMEOUT);
			connection.setConnectTimeout(TIMEOUT);

			final List<String> allowedHeaders = new ArrayList<>();
			allowedHeaders.add("user-agent");
			allowedHeaders.add("accept");
			allowedHeaders.add("accept-language");

			final Enumeration headerNames = request.getHeaderNames();
			while (headerNames.hasMoreElements()) {
				final String name = String.valueOf(headerNames.nextElement());
				final String value = request.getHeader(name);
				if (allowedHeaders.contains(name)) {
					logger.debug("set header " + name + "=" + value);
					connection.setRequestProperty(name, value);
				} else {
					logger.debug("skip header " + name + "=" + value);
				}
			}

			connection.connect();

			response.setContentType(connection.getContentType());

			inputStream = connection.getInputStream();
			outputStream = response.getOutputStream();
			streamUtil.copy(inputStream, outputStream);
		} finally {
			if (inputStream != null)
				inputStream.close();
			if (outputStream != null)
				outputStream.close();
		}
	}

	@Override
	public boolean isAdminRequired() {
		return false;
	}

	@Override
	public boolean isLoginRequired() {
		return false;
	}

	private final class HostnameVerifierAllowAll implements HostnameVerifier {

		@Override
		public boolean verify(final String arg0, final SSLSession arg1) {
			return true;
		}
	}

	private final class X509TrustManagerAllowAll implements X509TrustManager {

		@Override
		public java.security.cert.X509Certificate[] getAcceptedIssuers() {
			return null;
		}

		@Override
		public void checkClientTrusted(final java.security.cert.X509Certificate[] certs, final String authType) {
		}

		@Override
		public void checkServerTrusted(final java.security.cert.X509Certificate[] certs, final String authType) {
		}
	}
}
