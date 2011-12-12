package de.benjaminborbe.monitoring.check;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

import org.slf4j.Logger;

public class TcpCheck implements Check {

	private static final int TIMEOUT = 5 * 1000;

	private final int port;

	private final String hostname;

	private final Logger logger;

	public TcpCheck(final Logger logger, final String hostname, final int port) {
		this.logger = logger;
		this.hostname = hostname;
		this.port = port;
	}

	@Override
	public boolean check() {
		Socket socket = null;
		try {
			socket = new Socket();
			final SocketAddress endpoint = new InetSocketAddress(hostname, port);
			socket.connect(endpoint, TIMEOUT);
			return socket.isConnected();
		}
		catch (final Exception e) {
			logger.warn("check tcp-connect to " + hostname + ":" + port + " failed");
			return false;
		}
		finally {
			try {
				if (socket != null)
					socket.close();
			}
			catch (final IOException e) {
			}
		}
	}

	@Override
	public String getMessage() {
		return "TCP-Check host: " + hostname + ":" + port;
	}

}
