package de.benjaminborbe.index.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class HttpDownloaderImpl implements HttpDownloader {

	private final class HostnameVerifierAllowAll implements HostnameVerifier {

		@Override
		public boolean verify(final String arg0, final SSLSession arg1) {
			return true;
		}
	}

	private final class X509TrustManagerAllowAll implements X509TrustManager {

		public java.security.cert.X509Certificate[] getAcceptedIssuers() {
			return null;
		}

		public void checkClientTrusted(final java.security.cert.X509Certificate[] certs, final String authType) {
		}

		public void checkServerTrusted(final java.security.cert.X509Certificate[] certs, final String authType) {
			// System.out.println("authType is " + authType);
			// System.out.println("cert issuers");
			// for (int i = 0; i < certs.length; i++) {
			// System.out.println("\t" + certs[i].getIssuerX500Principal().getName());
			// System.out.println("\t" + certs[i].getIssuerDN().getName());
			// }
		}
	}

	private final Logger logger;

	private final StreamUtil streamUtil;

	private final DurationUtil durationUtil;

	@Inject
	public HttpDownloaderImpl(final Logger logger, final StreamUtil streamUtil, final DurationUtil durationUtil) {
		this.logger = logger;
		this.streamUtil = streamUtil;
		this.durationUtil = durationUtil;
	}

	@Override
	public HttpDownloadResult downloadUrlUnsecure(final URL url, final int timeout) throws IOException {
		final TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManagerAllowAll() };

		final SSLSocketFactory orgSSLSocketFactory = HttpsURLConnection.getDefaultSSLSocketFactory();
		final HostnameVerifier orgHostnameVerifier = HttpsURLConnection.getDefaultHostnameVerifier();
		try {
			final SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			final HostnameVerifier hv = new HostnameVerifierAllowAll();
			HttpsURLConnection.setDefaultHostnameVerifier(hv);
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

			return downloadUrl(url, timeout);
		}
		catch (final NoSuchAlgorithmException e) {
			logger.debug("NoSuchAlgorithmException", e);
			throw new IOException("NoSuchAlgorithmException", e);
		}
		catch (final KeyManagementException e) {
			logger.debug("KeyManagementException", e);
			throw new IOException("KeyManagementException", e);
		}
		finally {
			HttpsURLConnection.setDefaultHostnameVerifier(orgHostnameVerifier);
			HttpsURLConnection.setDefaultSSLSocketFactory(orgSSLSocketFactory);
		}
	}

	@Override
	public HttpDownloadResult downloadUrl(final URL url, final int timeout) throws IOException {
		logger.debug("downloadUrl started");
		final Duration duration = durationUtil.getDuration();
		InputStream inputStream = null;
		ByteArrayOutputStream outputStream = null;
		try {
			final URLConnection connection = url.openConnection();
			connection.setConnectTimeout(timeout);
			connection.setReadTimeout(timeout);
			final Encoding contentEncoding = new Encoding(connection.getContentEncoding());
			inputStream = connection.getInputStream();
			outputStream = new ByteArrayOutputStream();
			streamUtil.copy(inputStream, outputStream);
			final byte[] content = outputStream.toByteArray();
			final HttpDownloadResult httpDownloadResult = new HttpDownloadResult(duration.getTime(), content, contentEncoding);
			logger.debug("downloadUrl finished");
			return httpDownloadResult;
		}
		finally {
			if (inputStream != null)
				inputStream.close();
			if (outputStream != null)
				outputStream.close();
		}
	}
}
