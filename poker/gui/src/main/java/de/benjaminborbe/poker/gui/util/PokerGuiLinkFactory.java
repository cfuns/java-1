package de.benjaminborbe.poker.gui.util;

import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.poker.api.PokerGameIdentifier;
import de.benjaminborbe.poker.api.PokerPlayerIdentifier;
import de.benjaminborbe.poker.gui.PokerGuiConstants;
import de.benjaminborbe.tools.url.MapParameter;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.website.link.LinkRelativWidget;
import de.benjaminborbe.website.link.LinkWidget;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class PokerGuiLinkFactory {

	private final UrlUtil urlUtil;

	@Inject
	public PokerGuiLinkFactory(final UrlUtil urlUtil) {
		this.urlUtil = urlUtil;
	}

	public Widget back(final HttpServletRequest request) throws MalformedURLException {
		final String referer = request.getHeader("referer");
		return new LinkWidget(referer, "back");
	}

	public Widget gameCreate(final HttpServletRequest request) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + PokerGuiConstants.NAME + PokerGuiConstants.URL_GAME_CREATE, new MapParameter(), "create game");
	}

	public Widget gameDelete(final HttpServletRequest request, final PokerGameIdentifier id) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + PokerGuiConstants.NAME + PokerGuiConstants.URL_GAME_DELETE,
			new MapParameter().add(PokerGuiConstants.PARAMETER_GAME_ID, id), "delete").addConfirm("delete game?");
	}

	public Widget gameJoin(
		final HttpServletRequest request,
		final PokerGameIdentifier gameIdentifier,
		final PokerPlayerIdentifier playerIdentifier
	) throws MalformedURLException,
		UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + PokerGuiConstants.NAME + PokerGuiConstants.URL_GAME_JOIN, new MapParameter().add(PokerGuiConstants.PARAMETER_GAME_ID,
			gameIdentifier).add(PokerGuiConstants.PARAMETER_PLAYER_ID, playerIdentifier), "join");
	}

	public Widget gameLeave(
		final HttpServletRequest request,
		final PokerGameIdentifier gameIdentifier,
		final PokerPlayerIdentifier playerIdentifier
	) throws MalformedURLException,
		UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + PokerGuiConstants.NAME + PokerGuiConstants.URL_GAME_LEAVE, new MapParameter().add(PokerGuiConstants.PARAMETER_GAME_ID,
			gameIdentifier).add(PokerGuiConstants.PARAMETER_PLAYER_ID, playerIdentifier), "leave game");
	}

	public Widget gameList(final HttpServletRequest request) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + PokerGuiConstants.NAME + PokerGuiConstants.URL_GAME_LIST, new MapParameter(), "games");
	}

	public String gameListUrl(final HttpServletRequest request) {
		return request.getContextPath() + "/" + PokerGuiConstants.NAME + PokerGuiConstants.URL_GAME_LIST;
	}

	public Widget gameStart(final HttpServletRequest request, final PokerGameIdentifier id) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + PokerGuiConstants.NAME + PokerGuiConstants.URL_GAME_START,
			new MapParameter().add(PokerGuiConstants.PARAMETER_GAME_ID, id), "start game");
	}

	public Widget gameStop(final HttpServletRequest request, final PokerGameIdentifier id) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + PokerGuiConstants.NAME + PokerGuiConstants.URL_GAME_STOP, new MapParameter().add(PokerGuiConstants.PARAMETER_GAME_ID, id),
			"stop game");
	}

	public Widget gameView(
		final HttpServletRequest request,
		final PokerGameIdentifier id,
		final String name
	) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + PokerGuiConstants.NAME + PokerGuiConstants.URL_GAME_VIEW, new MapParameter().add(PokerGuiConstants.PARAMETER_GAME_ID, id),
			name);
	}

	public Widget playerCreate(final HttpServletRequest request) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + PokerGuiConstants.NAME + PokerGuiConstants.URL_PLAYER_CREATE, new MapParameter(), "create player");
	}

	public Widget playerDelete(final HttpServletRequest request, final PokerPlayerIdentifier id) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + PokerGuiConstants.NAME + PokerGuiConstants.URL_PLAYER_DELETE, new MapParameter().add(
			PokerGuiConstants.PARAMETER_PLAYER_ID, id), "delete").addConfirm("delete player?");
	}

	public Widget playerList(final HttpServletRequest request) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + PokerGuiConstants.NAME + PokerGuiConstants.URL_PLAYER_LIST, new MapParameter(), "players");
	}

	public String playerListUrl(final HttpServletRequest request) {
		return request.getContextPath() + "/" + PokerGuiConstants.NAME + PokerGuiConstants.URL_PLAYER_LIST;
	}

	public Widget playerView(
		final HttpServletRequest request,
		final PokerPlayerIdentifier id,
		final String name
	) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + PokerGuiConstants.NAME + PokerGuiConstants.URL_PLAYER_VIEW, new MapParameter().add(PokerGuiConstants.PARAMETER_PLAYER_ID,
			id), name);
	}

	public Widget call(
		final HttpServletRequest request,
		final PokerGameIdentifier gameIdentifier,
		final PokerPlayerIdentifier playerIdentifier
	) throws MalformedURLException,
		UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + PokerGuiConstants.NAME + PokerGuiConstants.URL_ACTION_CALL, new MapParameter().add(PokerGuiConstants.PARAMETER_GAME_ID,
			gameIdentifier).add(PokerGuiConstants.PARAMETER_PLAYER_ID, playerIdentifier), "call");
	}

	public Widget fold(
		final HttpServletRequest request,
		final PokerGameIdentifier gameIdentifier,
		final PokerPlayerIdentifier playerIdentifier
	) throws MalformedURLException,
		UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + PokerGuiConstants.NAME + PokerGuiConstants.URL_ACTION_FOLD, new MapParameter().add(PokerGuiConstants.PARAMETER_GAME_ID,
			gameIdentifier).add(PokerGuiConstants.PARAMETER_PLAYER_ID, playerIdentifier), "fold");
	}

	public Widget apiHelp(final HttpServletRequest request) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + PokerGuiConstants.NAME + PokerGuiConstants.URL_API_HELP, new MapParameter(), "Json - Api Help");
	}

	public Widget gameUpdate(final HttpServletRequest request, final PokerGameIdentifier id) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + PokerGuiConstants.NAME + PokerGuiConstants.URL_GAME_UPDATE,
			new MapParameter().add(PokerGuiConstants.PARAMETER_GAME_ID, id), "update");
	}

	public Widget playerUpdate(final HttpServletRequest request, final PokerPlayerIdentifier id) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + PokerGuiConstants.NAME + PokerGuiConstants.URL_PLAYER_UPDATE, new MapParameter().add(
			PokerGuiConstants.PARAMETER_PLAYER_ID, id), "update");
	}

	private String createReportName(final PokerPlayerIdentifier pokerPlayerIdentifier) {
		return "PokerPlayerAmount-" + pokerPlayerIdentifier + "_LATEST";
	}

	public Widget createHistoryLink(
		final HttpServletRequest request,
		final String name,
		final PokerPlayerIdentifier... playerIdentifiers
	) throws MalformedURLException, UnsupportedEncodingException {
		return createHistoryLink(request, name, Arrays.asList(playerIdentifiers));
	}

	public Widget createHistoryLink(
		final HttpServletRequest request,
		final String name,
		final List<PokerPlayerIdentifier> players
	) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/analytics/report/view", new MapParameter().add("chart_type", "LINECHART").add("report_interval", "MINUTE").add("report_id", createReportName(players)), name);
	}

	private Collection<String> createReportName(final Collection<PokerPlayerIdentifier> players) {
		final List<String> result = new ArrayList<String>();
		for (final PokerPlayerIdentifier pokerPlayerIdentifier : players) {
			result.add(createReportName(pokerPlayerIdentifier));
		}
		return result;
	}
}
