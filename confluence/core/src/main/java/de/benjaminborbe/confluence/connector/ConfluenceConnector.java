package de.benjaminborbe.confluence.connector;

import java.net.MalformedURLException;
import java.util.Collection;

public interface ConfluenceConnector {

	Collection<ConfluenceConnectorPageSummary> getPageSummaries(String confluenceBaseUrl, ConfluenceSession token, String spaceKey) throws MalformedURLException,
		ConfluenceXmlRpcClientException;

	String getRenderedContent(String confluenceBaseUrl, ConfluenceSession token, String pageId) throws MalformedURLException, ConfluenceXmlRpcClientException;

	String getRenderedContent(String confluenceBaseUrl, ConfluenceSession token, String spaceName, String pageName) throws MalformedURLException, ConfluenceXmlRpcClientException;

	Collection<String> getSpaceKeys(String confluenceBaseUrl, ConfluenceSession token) throws MalformedURLException, ConfluenceXmlRpcClientException;

	ConfluenceSession login(String confluenceBaseUrl, String username, String password) throws MalformedURLException, ConfluenceXmlRpcClientException;

	ConfluenceConnectorPage getPage(String confluenceBaseUrl, ConfluenceSession token, ConfluenceConnectorPageSummary confluenceConnectorPageSummary) throws MalformedURLException,
		ConfluenceXmlRpcClientException;

	ConfluenceConnectorPage getPage(String confluenceBaseUrl, ConfluenceSession token, String pageId) throws MalformedURLException, ConfluenceXmlRpcClientException;

}
