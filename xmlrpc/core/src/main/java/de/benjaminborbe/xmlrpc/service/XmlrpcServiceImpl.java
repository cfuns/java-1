package de.benjaminborbe.xmlrpc.service;

import de.benjaminborbe.xmlrpc.api.XmlrpcService;
import de.benjaminborbe.xmlrpc.api.XmlrpcServiceException;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.net.URL;

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
			logger.debug("call on url: " + url + " method: " + pMethodName + " with params " + toString(pParams));
			final XmlRpcClient client = getClient(url);

			// System.setProperty("javax.xml.parsers.SAXParserFactory",
			// "org.apache.xerces.jaxp.SAXParserFactoryImpl");

			return client.execute(pMethodName, pParams);
		} catch (final XmlRpcException e) {
			throw new XmlrpcServiceException(e);
		}
	}

	private String toString(final Object[] pParams) {
		final StringBuilder sb = new StringBuilder();
		if (pParams != null) {
			sb.append('[');
			for (final Object param : pParams) {
				sb.append(param != null ? String.valueOf(param) : "null");
			}
			sb.append(']');
		} else {
			sb.append("null");
		}
		return sb.toString();
	}

	private XmlRpcClient getClient(final URL url) {
		final XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
		config.setServerURL(url);
		final XmlRpcClient client = new XmlRpcClient();
		client.setConfig(config);
		return client;
	}

}
