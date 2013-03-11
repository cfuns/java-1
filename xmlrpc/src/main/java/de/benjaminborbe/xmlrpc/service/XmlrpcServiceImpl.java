package de.benjaminborbe.xmlrpc.service;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.xmlrpc.api.XmlrpcService;
import de.benjaminborbe.xmlrpc.api.XmlrpcServiceException;

@Singleton
public class XmlrpcServiceImpl implements XmlrpcService {

	private final Logger logger;

	@Inject
	public XmlrpcServiceImpl(final Logger logger) {
		this.logger = logger;
	}

	@Override
	public Object execute(final URL url, final String pMethodName, final Object[] pParams) throws XmlrpcServiceException {
		try {
			logger.trace("execute");
			final XmlRpcClient client = getClient(url);

			// System.setProperty("javax.xml.parsers.SAXParserFactory",
			// "org.apache.xerces.jaxp.SAXParserFactoryImpl");

			return client.execute(pMethodName, pParams);
		}
		catch (final XmlRpcException | MalformedURLException e) {
			throw new XmlrpcServiceException(e);
		}
	}

	private XmlRpcClient getClient(final URL url) throws MalformedURLException {
		final XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
		config.setServerURL(url);
		final XmlRpcClient client = new XmlRpcClient();
		client.setConfig(config);
		return client;
	}

}
