package de.benjaminborbe.confluence.connector;

import java.net.MalformedURLException;
import java.util.Collection;

import org.apache.xmlrpc.XmlRpcException;

public interface ConfluenceConnector {

	Collection<ConfluenceConnectorPageSummary> getPageSummaries(String confluenceBaseUrl, ConfluenceSession token, String spaceKey) throws MalformedURLException, XmlRpcException;

	String getRenderedContent(String confluenceBaseUrl, ConfluenceSession token, String pageId) throws MalformedURLException, XmlRpcException;

	String getRenderedContent(String confluenceBaseUrl, ConfluenceSession token, String spaceName, String pageName) throws MalformedURLException, XmlRpcException;

	Collection<String> getSpaceKeys(String confluenceBaseUrl, ConfluenceSession token) throws MalformedURLException, XmlRpcException;

	ConfluenceSession login(String confluenceBaseUrl, String username, String password) throws MalformedURLException, XmlRpcException;

	ConfluenceConnectorPage getPage(String confluenceBaseUrl, ConfluenceSession token, ConfluenceConnectorPageSummary confluenceConnectorPageSummary) throws MalformedURLException,
			XmlRpcException;

	ConfluenceConnectorPage getPage(String confluenceBaseUrl, ConfluenceSession token, String pageId) throws MalformedURLException, XmlRpcException;

}
