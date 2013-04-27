package de.benjaminborbe.message.gui.util;

import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.message.api.MessageIdentifier;
import de.benjaminborbe.message.gui.MessageGuiConstants;
import de.benjaminborbe.tools.url.MapParameter;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.website.link.LinkRelativWidget;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;

public class MessageGuiLinkFactory {

	private final UrlUtil urlUtil;

	@Inject
	public MessageGuiLinkFactory(final UrlUtil urlUtil) {
		this.urlUtil = urlUtil;
	}

	public Widget deleteByType(final HttpServletRequest request) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + MessageGuiConstants.NAME + MessageGuiConstants.URL_DELETE, new MapParameter(), "delete by type");
	}

	public Widget deleteMessage(
		final HttpServletRequest request,
		final MessageIdentifier messageIdentifier
	) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + MessageGuiConstants.NAME + MessageGuiConstants.URL_MESSAGE_DELETE, new MapParameter().add(
			MessageGuiConstants.PARAMETER_MESSAGE_ID, messageIdentifier), "delete").addConfirm("delete?");
	}

	public Widget unlockExpiredMessage(final HttpServletRequest request) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + MessageGuiConstants.NAME + MessageGuiConstants.URL_UNLOCK, new MapParameter(), "unlock expired messages");
	}

	public Widget listMessages(final HttpServletRequest request) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + MessageGuiConstants.NAME + MessageGuiConstants.URL_MESSAGE_LIST, new MapParameter(), "list messages");
	}

	public Widget exchangeMessages(final HttpServletRequest request) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + MessageGuiConstants.NAME + MessageGuiConstants.URL_MESSAGE_EXCHANGE, new MapParameter(), "exchange messages");
	}
}
