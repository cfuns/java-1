package de.benjaminborbe.confluence.connector;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.slf4j.Logger;

import de.benjaminborbe.analytics.api.AnalyticsReportIdentifier;
import de.benjaminborbe.analytics.api.AnalyticsService;

public class ConfluenceXmlRpcClient {

	private final XmlRpcClient client;

	private final AnalyticsService analyticsService;

	private final AnalyticsReportIdentifier analyticsReportIdentifier = new AnalyticsReportIdentifier("ConfluenceXmlRpcRequest");

	private final Logger logger;

	public ConfluenceXmlRpcClient(final Logger logger, final AnalyticsService analyticsService, final XmlRpcClient client) {
		this.logger = logger;
		this.analyticsService = analyticsService;
		this.client = client;
	}

	public Object execute(final String pMethodName, final Object[] pParams) throws ConfluenceXmlRpcClientException {
		try {
			track();
			return client.execute(pMethodName, pParams);
		}
		catch (final XmlRpcException e) {
			throw new ConfluenceXmlRpcClientException(e);
		}
	}

	private void track() {
		try {
			analyticsService.addReportValue(analyticsReportIdentifier);
		}
		catch (final Exception e) {
			logger.warn(e.getClass().getName(), e);
		}
	}

}
