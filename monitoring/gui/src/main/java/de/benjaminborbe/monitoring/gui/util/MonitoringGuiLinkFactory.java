package de.benjaminborbe.monitoring.gui.util;

import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.monitoring.api.MonitoringNodeIdentifier;
import de.benjaminborbe.monitoring.gui.MonitoringGuiConstants;
import de.benjaminborbe.tools.url.MapParameter;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.website.link.LinkRelativWidget;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;

public class MonitoringGuiLinkFactory {

	private final UrlUtil urlUtil;

	@Inject
	public MonitoringGuiLinkFactory(final UrlUtil urlUtil) {
		this.urlUtil = urlUtil;
	}

	public Widget check(final HttpServletRequest request) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + MonitoringGuiConstants.NAME + MonitoringGuiConstants.URL_TRIGGER_CHECK, new MapParameter(), "trigger check");
	}

	public Widget mail(final HttpServletRequest request) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + MonitoringGuiConstants.NAME + MonitoringGuiConstants.URL_TRIGGER_MAIL, new MapParameter(), "trigger mail");
	}

	public Widget nodeList(final HttpServletRequest request) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + MonitoringGuiConstants.NAME + MonitoringGuiConstants.URL_NODE_LIST, new MapParameter(), "view status");
	}

	public Widget nodeCreate(final HttpServletRequest request) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + MonitoringGuiConstants.NAME + MonitoringGuiConstants.URL_NODE_CREATE, new MapParameter(), "create node");
	}

	public Widget nodeUpdate(final HttpServletRequest request, final MonitoringNodeIdentifier id) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + MonitoringGuiConstants.NAME + MonitoringGuiConstants.URL_NODE_UPDATE, new MapParameter().add(
			MonitoringGuiConstants.PARAMETER_NODE_ID, id), "update");
	}

	public Widget nodeDelete(final HttpServletRequest request, final MonitoringNodeIdentifier id) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + MonitoringGuiConstants.NAME + MonitoringGuiConstants.URL_NODE_DELETE, new MapParameter().add(
			MonitoringGuiConstants.PARAMETER_NODE_ID, id), "delete").addConfirm("delete node?");
	}

	public String nodeListUrl(final HttpServletRequest request) {
		return request.getContextPath() + "/" + MonitoringGuiConstants.NAME + MonitoringGuiConstants.URL_NODE_LIST;
	}

	public Widget nodeSilent(final HttpServletRequest request, final MonitoringNodeIdentifier id) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + MonitoringGuiConstants.NAME + MonitoringGuiConstants.URL_NODE_SILENT, new MapParameter().add(
			MonitoringGuiConstants.PARAMETER_NODE_ID, id), "silent");
	}

	public Widget nodeUnsilent(final HttpServletRequest request, final MonitoringNodeIdentifier id) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + MonitoringGuiConstants.NAME + MonitoringGuiConstants.URL_NODE_UNSILENT, new MapParameter().add(
			MonitoringGuiConstants.PARAMETER_NODE_ID, id), "unsilent");
	}

	public Widget createNode(final HttpServletRequest request, final MonitoringNodeIdentifier id) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + MonitoringGuiConstants.NAME + MonitoringGuiConstants.URL_NODE_CREATE, new MapParameter().add(
			MonitoringGuiConstants.PARAMETER_NODE_PARENT_ID, id), "create subnode");
	}

	public Widget checkNode(final HttpServletRequest request, final MonitoringNodeIdentifier id) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + MonitoringGuiConstants.NAME + MonitoringGuiConstants.URL_NODE_CHECK, new MapParameter().add(
			MonitoringGuiConstants.PARAMETER_NODE_ID, id), "check");
	}

	public Widget nodeView(
		final HttpServletRequest request,
		final MonitoringNodeIdentifier id,
		final String name
	) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + MonitoringGuiConstants.NAME + MonitoringGuiConstants.URL_NODE_VIEW, new MapParameter().add(
			MonitoringGuiConstants.PARAMETER_NODE_ID, id), name);
	}

	public Widget nodesReset(final HttpServletRequest request) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + MonitoringGuiConstants.NAME + MonitoringGuiConstants.URL_NODES_RESET, new MapParameter(), "reset");
	}
}
