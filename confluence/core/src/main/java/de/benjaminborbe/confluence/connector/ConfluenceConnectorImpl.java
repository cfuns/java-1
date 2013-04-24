package de.benjaminborbe.confluence.connector;

import de.benjaminborbe.tools.util.ParseException;
import de.benjaminborbe.tools.util.ParseUtil;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Singleton
public class ConfluenceConnectorImpl implements ConfluenceConnector {

	private final Logger logger;

	private final ParseUtil parseUtil;

	private final ConfluenceXmlRpcClient confluenceXmlRpcClient;

	@Inject
	public ConfluenceConnectorImpl(final Logger logger, final ParseUtil parseUtil, final ConfluenceXmlRpcClient confluenceXmlRpcClient) {
		this.logger = logger;
		this.parseUtil = parseUtil;
		this.confluenceXmlRpcClient = confluenceXmlRpcClient;
	}

	@Override
	public ConfluenceSession login(final String confluenceBaseUrl, final String username, final String password) throws MalformedURLException, ConfluenceXmlRpcClientException, ParseException {
		logger.debug("login");
		try {
			final String token = (String) execute(confluenceBaseUrl, "confluence2.login", new Object[]{username, password});
			logger.debug("login success with version 2");
			return new ConfluenceSession(token, 2);
		} catch (final ConfluenceXmlRpcClientException e) {
			// nop
		}
		final String token = (String) execute(confluenceBaseUrl, "confluence1.login", new Object[]{username, password});
		logger.debug("login success with version 1");
		return new ConfluenceSession(token, 1);
	}

	private Object execute(final String confluenceBaseUrl, final String method, final Object[] objects) throws MalformedURLException, ConfluenceXmlRpcClientException, ParseException {
		return confluenceXmlRpcClient.execute(parseUtil.parseURL(confluenceBaseUrl + "/rpc/xmlrpc"), method, objects);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public String getRenderedContent(final String confluenceBaseUrl, final ConfluenceSession session, final String spaceName, final String pageName) throws MalformedURLException,
		ConfluenceXmlRpcClientException, ParseException {
		logger.debug("getRenderedContent");
		final Map page = (Map) execute(confluenceBaseUrl, "confluence" + session.getApiVersion() + ".getPage", new Object[]{session.getToken(), spaceName, pageName});
		final String pageId = (String) page.get("id");
		final String content = (String) page.get("content");

		return (String) execute(confluenceBaseUrl, "confluence" + session.getApiVersion() + ".renderContent", new Object[]{session.getToken(), "Main", pageId, content});
	}

	@Override
	public String getRenderedContent(final String confluenceBaseUrl, final ConfluenceSession session, final String pageId) throws MalformedURLException,
		ConfluenceXmlRpcClientException, ParseException {
		logger.debug("getRenderedContent");
		return (String) execute(confluenceBaseUrl, "confluence" + session.getApiVersion() + ".renderContent", new Object[]{session.getToken(), "Main", pageId, ""});
	}

	@SuppressWarnings({"rawtypes"})
	@Override
	public Collection<String> getSpaceKeys(final String confluenceBaseUrl, final ConfluenceSession session) throws MalformedURLException, ConfluenceXmlRpcClientException, ParseException {
		logger.debug("getSpaceKeys");
		final Object[] spaces = (Object[]) execute(confluenceBaseUrl, "confluence" + session.getApiVersion() + ".getSpaces", new Object[]{session.getToken()});
		final List<String> result = new ArrayList<>();
		for (final Object spaceObject : spaces) {
			final Map space = (Map) spaceObject;
			result.add(String.valueOf(space.get("key")));
		}
		return result;
	}

	@Override
	public ConfluenceConnectorPage getPage(final String confluenceBaseUrl, final ConfluenceSession session, final ConfluenceConnectorPageSummary confluenceConnectorPageSummary)
		throws MalformedURLException, ConfluenceXmlRpcClientException, ParseException {
		return getPage(confluenceBaseUrl, session, confluenceConnectorPageSummary.getPageId());
	}

	@SuppressWarnings({"rawtypes"})
	@Override
	public ConfluenceConnectorPage getPage(final String confluenceBaseUrl, final ConfluenceSession session, final String pageId) throws MalformedURLException,
		ConfluenceXmlRpcClientException, ParseException {
		logger.debug("getPages");
		final Object pageObject = execute(confluenceBaseUrl, "confluence" + session.getApiVersion() + ".getPage", new Object[]{session.getToken(), pageId});
		final Map page = (Map) pageObject;
		return new ConfluenceConnectorPage(String.valueOf(page.get("id")), String.valueOf(page.get("url")), String.valueOf(page.get("title")), toDate(page.get("modified")));
	}

	private Date toDate(final Object object) {
		if (object != null && object instanceof Date) {
			return (Date) object;
		} else {
			return null;
		}
	}

	@SuppressWarnings({"rawtypes"})
	@Override
	public Collection<ConfluenceConnectorPageSummary> getPageSummaries(final String confluenceBaseUrl, final ConfluenceSession session, final String spaceKey)
		throws MalformedURLException, ConfluenceXmlRpcClientException, ParseException {
		logger.debug("getPages");
		final Object[] pages = (Object[]) execute(confluenceBaseUrl, "confluence" + session.getApiVersion() + ".getPages", new Object[]{session.getToken(), spaceKey});
		final List<ConfluenceConnectorPageSummary> result = new ArrayList<>();
		for (final Object pageObject : pages) {
			final Map page = (Map) pageObject;
			result.add(new ConfluenceConnectorPageSummary(String.valueOf(page.get("id")), String.valueOf(page.get("url")), String.valueOf(page.get("title"))));
		}
		return result;
	}
}
