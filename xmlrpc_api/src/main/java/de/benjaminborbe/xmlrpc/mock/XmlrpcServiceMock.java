package de.benjaminborbe.xmlrpc.mock;

import java.net.URL;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.xmlrpc.api.XmlrpcService;
import de.benjaminborbe.xmlrpc.api.XmlrpcServiceException;

@Singleton
public class XmlrpcServiceMock implements XmlrpcService {

	@Inject
	public XmlrpcServiceMock() {
	}

	@Override
	public Object execute(final URL url, final String pMethodName, final Object[] pParams) throws XmlrpcServiceException {
		return null;
	}
}
