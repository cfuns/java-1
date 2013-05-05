package de.benjaminborbe.confluence.connector;

public class ConfluenceConnectorSession {

	private final String token;

	private final int apiVersion;

	private final String baseUrl;

	public ConfluenceConnectorSession(final String token, final int apiVersion, final String baseUrl) {
		this.token = token;
		this.apiVersion = apiVersion;
		this.baseUrl = baseUrl;
	}

	public String getBaseUrl() {
		return baseUrl;
	}

	public String getToken() {
		return token;
	}

	public int getApiVersion() {
		return apiVersion;
	}

}
