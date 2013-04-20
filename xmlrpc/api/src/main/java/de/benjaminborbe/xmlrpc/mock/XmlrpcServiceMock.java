package de.benjaminborbe.xmlrpc.mock;

import de.benjaminborbe.xmlrpc.api.XmlrpcService;
import de.benjaminborbe.xmlrpc.api.XmlrpcServiceException;

import java.net.URL;

public class XmlrpcServiceMock implements XmlrpcService {

	public XmlrpcServiceMock() {
	}

	@Override
	public Object execute(final URL url, final String pMethodName, final Object[] pParams) throws XmlrpcServiceException {
		return null;
	}
}
