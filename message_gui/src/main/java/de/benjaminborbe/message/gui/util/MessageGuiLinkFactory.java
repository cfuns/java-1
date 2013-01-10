package de.benjaminborbe.message.gui.util;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;

import javax.servlet.http.HttpServletRequest;

import com.google.inject.Inject;

import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.message.gui.MessageGuiConstants;
import de.benjaminborbe.tools.url.MapParameter;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.website.link.LinkRelativWidget;

public class MessageGuiLinkFactory {

	private final UrlUtil urlUtil;

	@Inject
	public MessageGuiLinkFactory(final UrlUtil urlUtil) {
		this.urlUtil = urlUtil;
	}

	public Widget deleteByType(final HttpServletRequest request) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + MessageGuiConstants.NAME + MessageGuiConstants.URL_DELETE, new MapParameter(), "delete by type");
	}

	public Widget unlockExpiredMessage(final HttpServletRequest request) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + MessageGuiConstants.NAME + MessageGuiConstants.URL_UNLOCK, new MapParameter(), "unlock expired messages");
	}

}
