package de.benjaminborbe.confluence.connector;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.slf4j.Logger;

import com.google.inject.Inject;

import de.benjaminborbe.analytics.api.AnalyticsService;

public class ConfluenceXmlRpcClientUtil {

	private final Logger logger;

	private final AnalyticsService analyticsService;

	@Inject
	public ConfluenceXmlRpcClientUtil(final Logger logger, final AnalyticsService analyticsService) {
		this.logger = logger;
		this.analyticsService = analyticsService;
	}

	public ConfluenceXmlRpcClient getClient(final String confluenceBaseUrl) throws MalformedURLException {
		final URL url = new URL(confluenceBaseUrl + "/rpc/xmlrpc");
		final XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
		config.setServerURL(url);
		// config.setConnectionTimeout(ConfluenceConstants.CONNECTION_TIMEOUT);
		// config.setReplyTimeout(ConfluenceConstants.CONNECTION_TIMEOUT);
		final XmlRpcClient client = new XmlRpcClient();
		client.setConfig(config);
		return new ConfluenceXmlRpcClient(logger, analyticsService, client);
	}
}
