package de.benjaminborbe.vnc.core.config;

public class VncConfig {

	private final String hostname = "192.168.2.103";

	private final int port = 5900;

	private final String password = "mazdazx";

	public VncConfig() {
	}

	public String getHostname() {
		return hostname;
	}

	public int getPort() {
		return port;
	}

	public String getPassword() {
		return password;
	}

}
