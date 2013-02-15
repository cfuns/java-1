package de.benjaminborbe.poker.gui.util;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;

import javax.servlet.http.HttpServletRequest;

import com.google.inject.Inject;

import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.poker.api.PokerGameIdentifier;
import de.benjaminborbe.poker.api.PokerPlayerIdentifier;
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

	public Widget gameCreate(final HttpServletRequest request) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + PokerGuiConstants.NAME + PokerGuiConstants.URL_GAME_CREATE, new MapParameter(), "create game");
	}

	public Widget gameView(final HttpServletRequest request, final PokerGameIdentifier id) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + PokerGuiConstants.NAME + PokerGuiConstants.URL_GAME_VIEW, new MapParameter().add(PokerGuiConstants.PARAMETER_GAME_ID, id),
				"view");
	}

	public Widget gameList(final HttpServletRequest request) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + PokerGuiConstants.NAME + PokerGuiConstants.URL_GAME_LIST, new MapParameter(), "games");
	}

	public Widget playerList(final HttpServletRequest request) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + PokerGuiConstants.NAME + PokerGuiConstants.URL_PLAYER_LIST, new MapParameter(), "players");
	}

	public Widget playerView(final HttpServletRequest request, final PokerPlayerIdentifier id) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + PokerGuiConstants.NAME + PokerGuiConstants.URL_PLAYER_VIEW, new MapParameter().add(PokerGuiConstants.PARAMETER_PLAYER_ID,
				id), "view");
	}

	public Widget playerDelete(final HttpServletRequest request, final PokerPlayerIdentifier id) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + PokerGuiConstants.NAME + PokerGuiConstants.URL_PLAYER_DELETE, new MapParameter().add(
				PokerGuiConstants.PARAMETER_PLAYER_ID, id), "delete");
	}

	public Widget playerCreate(final HttpServletRequest request) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + PokerGuiConstants.NAME + PokerGuiConstants.URL_PLAYER_CREATE, new MapParameter(), "create player");
	}

	public Widget gameDelete(final HttpServletRequest request, final PokerGameIdentifier id) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + PokerGuiConstants.NAME + PokerGuiConstants.URL_GAME_DELETE,
				new MapParameter().add(PokerGuiConstants.PARAMETER_GAME_ID, id), "delete");
	}

	public Widget startGame(final HttpServletRequest request, final PokerGameIdentifier id) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + PokerGuiConstants.NAME + PokerGuiConstants.URL_GAME_START,
				new MapParameter().add(PokerGuiConstants.PARAMETER_GAME_ID, id), "start game");
	}
}
