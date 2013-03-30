package de.benjaminborbe.tools.ssl;

import javax.net.SocketFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

public class SSLSocketFactoryBase extends SSLSocketFactory {

	private SSLSocketFactory socketFactory;

	public SSLSocketFactoryBase() {
		try {
			final SSLContext ctx = getSSLContext();
			socketFactory = ctx.getSocketFactory();
		} catch (final Exception ex) {
			socketFactory = null;
		}
	}

	private SSLContext getSSLContext() throws NoSuchAlgorithmException, KeyStoreException, CertificateException, IOException, KeyManagementException {
		// load your key store as a stream and initialize a KeyStore
		final InputStream trustStream = getClass().getClassLoader().getResourceAsStream(getKeyStorePath());
		final KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());

		// if your store is password protected then declare it (it can be null however)
		final char[] trustPassword = getPassword().toCharArray();// new char[0];

		// load the stream to your store
		trustStore.load(trustStream, trustPassword);

		// initialize a trust manager factory with the trusted store
		final TrustManagerFactory trustFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
		trustFactory.init(trustStore);

		// get the trust managers from the factory
		final TrustManager[] trustManagers = trustFactory.getTrustManagers();

		// initialize an ssl context to use these managers and set as default
		final SSLContext sslContext = SSLContext.getInstance("SSL");
		sslContext.init(null, trustManagers, null);
		return sslContext;
	}

	private String getPassword() {
		return "mazdazx";
	}

	public String getKeyStorePath() {
		return "keystore.jks";
	}

	public static SocketFactory getDefault() {
		return new SSLSocketFactoryBase();
	}

	@Override
	public String[] getDefaultCipherSuites() {
		return socketFactory.getDefaultCipherSuites();
	}

	@Override
	public String[] getSupportedCipherSuites() {
		return socketFactory.getSupportedCipherSuites();
	}

	@Override
	public Socket createSocket(final Socket socket, final String string, final int i, final boolean bln) throws IOException {
		return socketFactory.createSocket(socket, string, i, bln);
	}

	@Override
	public Socket createSocket(final String string, final int i) throws IOException {
		return socketFactory.createSocket(string, i);
	}

	@Override
	public Socket createSocket(final String string, final int i, final InetAddress ia, final int i1) throws IOException {
		return socketFactory.createSocket(string, i, ia, i1);
	}

	@Override
	public Socket createSocket(final InetAddress ia, final int i) throws IOException {
		return socketFactory.createSocket(ia, i);
	}

	@Override
	public Socket createSocket(final InetAddress ia, final int i, final InetAddress ia1, final int i1) throws IOException {
		return socketFactory.createSocket(ia, i, ia1, i1);
	}
}
