package de.benjaminborbe.confluence.connector;

import java.net.MalformedURLException;
import java.util.Collection;

import org.apache.xmlrpc.XmlRpcException;

public interface ConfluenceConnector {

	Collection<ConfluenceConnectorPage> getPages(String confluenceBaseUrl, String token, String spaceKey) throws MalformedURLException, XmlRpcException;

	String getRenderedContent(String confluenceBaseUrl, String token, String pageId) throws MalformedURLException, XmlRpcException;

	String getRenderedContent(String confluenceBaseUrl, String token, String spaceName, String pageName) throws MalformedURLException, XmlRpcException;

	Collection<String> getSpaceKeys(String confluenceBaseUrl, String token) throws MalformedURLException, XmlRpcException;

	String login(String confluenceBaseUrl, String username, String password) throws MalformedURLException, XmlRpcException;

}
