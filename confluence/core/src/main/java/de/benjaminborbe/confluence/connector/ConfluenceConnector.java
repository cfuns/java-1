package de.benjaminborbe.confluence.connector;

import de.benjaminborbe.confluence.dao.ConfluenceInstanceBean;
import de.benjaminborbe.tools.util.ParseException;

import java.net.MalformedURLException;
import java.util.Collection;
import java.util.List;

public interface ConfluenceConnector {

	Collection<ConfluenceConnectorPageSummary> getPageSummaries(ConfluenceConnectorSession token, String spaceKey) throws MalformedURLException,
		ConfluenceXmlRpcClientException, ParseException;

	List<ConfluenceConnectorLabel> getLabels(
		ConfluenceConnectorSession token,
		String pageId
	) throws ParseException, ConfluenceXmlRpcClientException, MalformedURLException;

	String getRenderedContent(
		ConfluenceConnectorSession token,
		String pageId
	) throws MalformedURLException, ConfluenceXmlRpcClientException, ParseException;

	String getRenderedContent(
		ConfluenceConnectorSession token,
		String spaceName,
		String pageName
	) throws MalformedURLException, ConfluenceXmlRpcClientException, ParseException;

	Collection<String> getSpaceKeys(
		ConfluenceConnectorSession token
	) throws MalformedURLException, ConfluenceXmlRpcClientException, ParseException;

	ConfluenceConnectorSession login(
		String confluenceBaseUrl,
		String username,
		String password
	) throws MalformedURLException, ConfluenceXmlRpcClientException, ParseException;

	ConfluenceConnectorPage getPage(
		ConfluenceConnectorSession token,
		ConfluenceConnectorPageSummary confluenceConnectorPageSummary
	) throws MalformedURLException,
		ConfluenceXmlRpcClientException, ParseException;

	ConfluenceConnectorPage getPage(
		ConfluenceConnectorSession token,
		String pageId
	) throws MalformedURLException, ConfluenceXmlRpcClientException, ParseException;

	ConfluenceConnectorSession login(ConfluenceInstanceBean confluenceInstanceBean) throws ParseException, ConfluenceXmlRpcClientException, MalformedURLException;
}
