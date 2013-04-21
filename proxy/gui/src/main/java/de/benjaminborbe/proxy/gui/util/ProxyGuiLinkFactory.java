package de.benjaminborbe.proxy.gui.util;

import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.proxy.api.ProxyConversationIdentifier;
import de.benjaminborbe.proxy.gui.ProxyGuiConstants;
import de.benjaminborbe.tools.url.MapParameter;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.website.link.LinkRelativWidget;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;

public class ProxyGuiLinkFactory {

	private final UrlUtil urlUtil;

	@Inject
	public ProxyGuiLinkFactory(final UrlUtil urlUtil) {
		this.urlUtil = urlUtil;
	}

	public Widget startProxy(final HttpServletRequest request) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + ProxyGuiConstants.NAME + ProxyGuiConstants.URL_START, new MapParameter(), "start");
	}

	public Widget stopProxy(final HttpServletRequest request) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + ProxyGuiConstants.NAME + ProxyGuiConstants.URL_STOP, new MapParameter(), "stop");
	}

	public Widget conversationDetails(final HttpServletRequest request, final ProxyConversationIdentifier proxyConversationIdentifier) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + ProxyGuiConstants.NAME + ProxyGuiConstants.URL_CONVERSATION_DETAILS, new MapParameter().add(ProxyGuiConstants.PARAMETER_CONVERSATION_ID, proxyConversationIdentifier.getId()), proxyConversationIdentifier.getId());
	}

	public Widget conversationList(final HttpServletRequest request) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + ProxyGuiConstants.NAME + ProxyGuiConstants.URL_CONVERSATION_LIST, new MapParameter(), "conversations");
	}
}
