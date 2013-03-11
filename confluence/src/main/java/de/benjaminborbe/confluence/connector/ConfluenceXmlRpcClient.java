package de.benjaminborbe.confluence.connector;

import java.net.URL;

import org.slf4j.Logger;

import com.google.inject.Inject;

import de.benjaminborbe.analytics.api.AnalyticsReportIdentifier;
import de.benjaminborbe.analytics.api.AnalyticsService;
import de.benjaminborbe.xmlrpc.api.XmlrpcService;
import de.benjaminborbe.xmlrpc.api.XmlrpcServiceException;

public class ConfluenceXmlRpcClient {

	private final AnalyticsService analyticsService;

	private final AnalyticsReportIdentifier analyticsReportIdentifier = new AnalyticsReportIdentifier("ConfluenceXmlRpcRequest");

	private final Logger logger;

	private final XmlrpcService xmlrpcService;

	@Inject
	public ConfluenceXmlRpcClient(final Logger logger, final AnalyticsService analyticsService, final XmlrpcService xmlrpcService) {
		this.logger = logger;
		this.analyticsService = analyticsService;
		this.xmlrpcService = xmlrpcService;
	}

	public Object execute(final URL url, final String pMethodName, final Object[] pParams) throws ConfluenceXmlRpcClientException {
		try {
			track();
			return xmlrpcService.execute(url, pMethodName, pParams);
		}
		catch (final XmlrpcServiceException e) {
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
