package de.benjaminborbe.tools.ssl;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class TrustStore {

	public static void main(final String[] args) {
		try {

			System.out.println("trustStore:" + System.getProperty("javax.net.ssl.trustStore"));
			final URL url = new URL("https://www.google.de/");
			final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			final OutputStreamWriter o = new OutputStreamWriter(conn.getOutputStream());
			o.write("GET /");
			o.flush();
		} catch (final Exception e) {
			System.out.println("Exception: " + e.getMessage());
			e.printStackTrace();
		}
	}

}
