package de.benjaminborbe.poker.gui.util;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;

import javax.servlet.http.HttpServletRequest;

import com.google.inject.Inject;

import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.poker.gui.PokerGuiConstants;
import de.benjaminborbe.tools.url.MapParameter;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.website.link.LinkRelativWidget;

public class PokerGuiLinkFactory {

	private final UrlUtil urlUtil;

	@Inject
	public PokerGuiLinkFactory(final UrlUtil urlUtil) {
		this.urlUtil = urlUtil;
	}

	public Widget createGame(final HttpServletRequest request) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + PokerGuiConstants.NAME + PokerGuiConstants.URL_GAME_CREATE, new MapParameter(), "create game");
	}

}
