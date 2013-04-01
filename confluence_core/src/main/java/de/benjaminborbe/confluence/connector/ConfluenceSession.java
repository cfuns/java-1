package de.benjaminborbe.confluence.connector;

public class ConfluenceSession {

	private final String token;

	private final int apiVersion;

	public ConfluenceSession(final String token, final int apiVersion) {
		this.token = token;
		this.apiVersion = apiVersion;
	}

	public String getToken() {
		return token;
	}

	public int getApiVersion() {
		return apiVersion;
	}

}
