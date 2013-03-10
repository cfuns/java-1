package de.benjaminborbe.xmlrpc.api;

import java.net.URL;

public interface XmlrpcService {

	Object execute(URL url, final String pMethodName, final Object[] pParams) throws XmlrpcServiceException;
}
