package de.benjaminborbe.confluence.connector;

import java.net.MalformedURLException;

import org.apache.xmlrpc.XmlRpcException;

public interface ConfluenceConnector {

	String getRenderedContent(String confluenceBaseUrl, String username, String password, String spaceName, String pageName) throws MalformedURLException, XmlRpcException;

}
