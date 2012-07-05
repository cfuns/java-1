package de.benjaminborbe.confluence.connector;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class ConfluenceConnectorImpl implements ConfluenceConnector {

	@Inject
	public ConfluenceConnectorImpl() {
		super();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public String getRenderedContent(final String confluenceBaseUrl, final String username, final String password, final String spaceName, final String pageName)
			throws MalformedURLException, XmlRpcException {
		final URL url = new URL(confluenceBaseUrl + "/rpc/xmlrpc");
		final XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
		config.setServerURL(url);
		final XmlRpcClient client = new XmlRpcClient();
		client.setConfig(config);

		final String token = (String) client.execute("confluence1.login", new Object[] { username, password });

		final Map page = (Map) client.execute("confluence1.getPage", new Object[] { token, spaceName, pageName });
		final String pageId = (String) page.get("id");
		final String content = (String) page.get("content");

		return (String) client.execute("confluence1.renderContent", new Object[] { token, "Main", pageId, content });
	}
}
