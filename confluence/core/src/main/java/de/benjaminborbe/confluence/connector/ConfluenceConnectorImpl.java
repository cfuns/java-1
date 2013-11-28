package de.benjaminborbe.confluence.connector;

import de.benjaminborbe.confluence.dao.ConfluenceInstanceBean;
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
	public ConfluenceConnectorSession login(final String confluenceBaseUrl, final String username, final String password) throws MalformedURLException,
		ConfluenceXmlRpcClientException, ParseException {
		logger.debug("login");
		try {
			final String token = (String) execute(confluenceBaseUrl, "confluence2.login", new Object[]{username, password});
			logger.debug("login success with version 2");
			return new ConfluenceConnectorSession(token, 2, confluenceBaseUrl);
		} catch (final ConfluenceXmlRpcClientException e) {
			// nop
		}
		final String token = (String) execute(confluenceBaseUrl, "confluence1.login", new Object[]{username, password});
		logger.debug("login success with version 1");
		return new ConfluenceConnectorSession(token, 1, confluenceBaseUrl);
	}

	private Object execute(
		final String confluenceBaseUrl,
		final String method,
		final Object[] objects
	) throws MalformedURLException, ConfluenceXmlRpcClientException, ParseException {
		return confluenceXmlRpcClient.execute(parseUtil.parseURL(confluenceBaseUrl + "/rpc/xmlrpc"), method, objects);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public String getRenderedContent(final ConfluenceConnectorSession session, final String spaceName, final String pageName) throws MalformedURLException,
		ConfluenceXmlRpcClientException, ParseException {
		logger.debug("getRenderedContent");
		final Map page = (Map) execute(session.getBaseUrl(), "confluence" + session.getApiVersion() + ".getPage", new Object[]{session.getToken(), spaceName, pageName});
		final String pageId = (String) page.get("id");
		final String content = (String) page.get("content");

		return (String) execute(session.getBaseUrl(), "confluence" + session.getApiVersion() + ".renderContent", new Object[]{session.getToken(), "Main", pageId, content});
	}

	@Override
	public String getRenderedContent(
		final ConfluenceConnectorSession session,
		final String pageId
	) throws MalformedURLException, ConfluenceXmlRpcClientException, ParseException {
		logger.debug("getRenderedContent");
		return (String) execute(session.getBaseUrl(), "confluence" + session.getApiVersion() + ".renderContent", new Object[]{session.getToken(), "Main", pageId, ""});
	}

	@SuppressWarnings({"rawtypes"})
	@Override
	public Collection<String> getSpaceKeys(final ConfluenceConnectorSession session) throws MalformedURLException, ConfluenceXmlRpcClientException, ParseException {
		logger.debug("getSpaceKeys");
		final Object[] spaces = (Object[]) execute(session.getBaseUrl(), "confluence" + session.getApiVersion() + ".getSpaces", new Object[]{session.getToken()});
		final List<String> result = new ArrayList<String>();
		for (final Object spaceObject : spaces) {
			final Map space = (Map) spaceObject;
			result.add(String.valueOf(space.get("key")));
		}
		return result;
	}

	@Override
	public ConfluenceConnectorPage getPage(final ConfluenceConnectorSession session, final ConfluenceConnectorPageSummary confluenceConnectorPageSummary)
		throws MalformedURLException, ConfluenceXmlRpcClientException, ParseException {
		return getPage(session, confluenceConnectorPageSummary.getPageId());
	}

	@SuppressWarnings({"rawtypes"})
	@Override
	public ConfluenceConnectorPage getPage(
		final ConfluenceConnectorSession session,
		final String pageId
	) throws MalformedURLException, ConfluenceXmlRpcClientException,
		ParseException {
		logger.debug("getPages");
		final Object pageObject = execute(session.getBaseUrl(), "confluence" + session.getApiVersion() + ".getPage", new Object[]{session.getToken(), pageId});
		final Map page = (Map) pageObject;
		return new ConfluenceConnectorPage(String.valueOf(page.get("id")), String.valueOf(page.get("url")), String.valueOf(page.get("title")), toDate(page.get("modified")));
	}

	@Override
	public ConfluenceConnectorSession login(final ConfluenceInstanceBean confluenceInstanceBean) throws ParseException, ConfluenceXmlRpcClientException, MalformedURLException {
		final String confluenceBaseUrl = confluenceInstanceBean.getUrl();
		final String username = confluenceInstanceBean.getUsername();
		final String password = confluenceInstanceBean.getPassword();
		return login(confluenceBaseUrl, username, password);
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
	public Collection<ConfluenceConnectorPageSummary> getPageSummaries(
		final ConfluenceConnectorSession session,
		final String spaceKey
	) throws MalformedURLException,
		ConfluenceXmlRpcClientException, ParseException {
		logger.debug("getPages");
		final Object[] pages = (Object[]) execute(session.getBaseUrl(), "confluence" + session.getApiVersion() + ".getPages", new Object[]{session.getToken(), spaceKey});
		final List<ConfluenceConnectorPageSummary> result = new ArrayList<ConfluenceConnectorPageSummary>();
		for (final Object pageObject : pages) {
			final Map page = (Map) pageObject;
			result.add(new ConfluenceConnectorPageSummary(String.valueOf(page.get("id")), String.valueOf(page.get("url")), String.valueOf(page.get("title"))));
		}
		return result;
	}

	@Override
	public List<ConfluenceConnectorLabel> getLabels(
		final ConfluenceConnectorSession session,
		final String objectId
	) throws ParseException, ConfluenceXmlRpcClientException,
		MalformedURLException {
		logger.debug("getLabels");
		final Object[] labels = (Object[]) execute(session.getBaseUrl(), "confluence" + session.getApiVersion() + ".getLabelsById", new Object[]{session.getToken(), objectId});
		final List<ConfluenceConnectorLabel> result = new ArrayList<ConfluenceConnectorLabel>();
		if (labels != null) {
			for (final Object labelObject : labels) {
				final Map<?, ?> label = (Map<?, ?>) labelObject;
				result.add(new ConfluenceConnectorLabel(String.valueOf(label.get("name"))));
			}
		}
		return result;
	}
}
