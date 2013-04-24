package de.benjaminborbe.confluence.connector;

import de.benjaminborbe.tools.util.ParseException;

import java.net.MalformedURLException;
import java.util.Collection;

public interface ConfluenceConnector {

	Collection<ConfluenceConnectorPageSummary> getPageSummaries(String confluenceBaseUrl, ConfluenceSession token, String spaceKey) throws MalformedURLException,
		ConfluenceXmlRpcClientException, ParseException;

	String getRenderedContent(String confluenceBaseUrl, ConfluenceSession token, String pageId) throws MalformedURLException, ConfluenceXmlRpcClientException, ParseException;

	String getRenderedContent(String confluenceBaseUrl, ConfluenceSession token, String spaceName, String pageName) throws MalformedURLException, ConfluenceXmlRpcClientException, ParseException;

	Collection<String> getSpaceKeys(String confluenceBaseUrl, ConfluenceSession token) throws MalformedURLException, ConfluenceXmlRpcClientException, ParseException;

	ConfluenceSession login(String confluenceBaseUrl, String username, String password) throws MalformedURLException, ConfluenceXmlRpcClientException, ParseException;

	ConfluenceConnectorPage getPage(String confluenceBaseUrl, ConfluenceSession token, ConfluenceConnectorPageSummary confluenceConnectorPageSummary) throws MalformedURLException,
		ConfluenceXmlRpcClientException, ParseException;

	ConfluenceConnectorPage getPage(String confluenceBaseUrl, ConfluenceSession token, String pageId) throws MalformedURLException, ConfluenceXmlRpcClientException, ParseException;

}
