package de.benjaminborbe.monitoring.gui.util;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;

import javax.servlet.http.HttpServletRequest;

import com.google.inject.Inject;

import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.monitoring.api.MonitoringNodeIdentifier;
import de.benjaminborbe.monitoring.gui.MonitoringGuiConstants;
import de.benjaminborbe.tools.url.MapParameter;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.website.link.LinkRelativWidget;

public class MonitoringGuiLinkFactory {

	private final UrlUtil urlUtil;

	@Inject
	public MonitoringGuiLinkFactory(final UrlUtil urlUtil) {
		this.urlUtil = urlUtil;
	}

	public Widget nodeList(final HttpServletRequest request) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + MonitoringGuiConstants.NAME + MonitoringGuiConstants.URL_NODE_LIST, new MapParameter(), "node list");
	}

	public Widget createNode(final HttpServletRequest request) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + MonitoringGuiConstants.NAME + MonitoringGuiConstants.URL_NODE_CREATE, new MapParameter(), "create node");
	}

	public Widget nodeUpdate(final HttpServletRequest request, final MonitoringNodeIdentifier id) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + MonitoringGuiConstants.NAME + MonitoringGuiConstants.URL_NODE_UPDATE, new MapParameter().add(
				MonitoringGuiConstants.PARAMETER_NODE_ID, id), "update");
	}

	public Widget nodeDelete(final HttpServletRequest request, final MonitoringNodeIdentifier id) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + MonitoringGuiConstants.NAME + MonitoringGuiConstants.URL_NODE_DELETE, new MapParameter().add(
				MonitoringGuiConstants.PARAMETER_NODE_ID, id), "delete");
	}

	public String nodeListUrl(final HttpServletRequest request) {
		return request.getContextPath() + "/" + MonitoringGuiConstants.NAME + MonitoringGuiConstants.URL_NODE_LIST;
	}

}
