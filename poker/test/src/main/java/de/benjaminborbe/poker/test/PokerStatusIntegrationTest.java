package de.benjaminborbe.poker.test;

import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.configuration.api.ConfigurationIdentifier;
import de.benjaminborbe.configuration.api.ConfigurationService;
import de.benjaminborbe.configuration.api.ConfigurationServiceException;
import de.benjaminborbe.lib.servlet.mock.HttpServletRequestMock;
import de.benjaminborbe.lib.servlet.mock.HttpServletResponseMock;
import de.benjaminborbe.poker.api.PokerGameDto;
import de.benjaminborbe.poker.api.PokerGameIdentifier;
import de.benjaminborbe.poker.api.PokerPlayerDto;
import de.benjaminborbe.poker.api.PokerPlayerIdentifier;
import de.benjaminborbe.poker.api.PokerService;
import de.benjaminborbe.test.osgi.TestUtil;
import de.benjaminborbe.tools.json.JSONObject;
import de.benjaminborbe.tools.json.JSONParseException;
import de.benjaminborbe.tools.json.JSONParser;
import de.benjaminborbe.tools.json.JSONParserSimple;
import org.apache.felix.http.api.ExtHttpService;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import javax.servlet.Servlet;
import java.util.ArrayList;

public class PokerStatusIntegrationTest extends PokerIntegrationTest {

	private final String validateEmailBaseUrl = "http://example.com/test";

	private final String shortenUrl = "http://bb/bb/s";

	public void testSimpleGame() throws Exception {
		/*
		 * Arrange
		 */
		try {
			final ConfigurationService configurationService = getConfigurationService();
			final ConfigurationIdentifier pokerJsonApiEnabledConfigurationIdentifier = configurationService.createConfigurationIdentifier("PokerJsonApiEnabled");
			configurationService.setConfigurationValue(pokerJsonApiEnabledConfigurationIdentifier, "true");
			final String configurationValue = configurationService.getConfigurationValue(pokerJsonApiEnabledConfigurationIdentifier);
			assertEquals("true", configurationValue);
		} catch (ConfigurationServiceException e) {
			fail("unexpected exception: " + e);
		}

		final SessionIdentifier sessionIdentifier = new SessionIdentifier(TestUtil.SESSION_ID);
		final AuthenticationService authenticationService = getAuthenticationService();
		final UserIdentifier userIdentifier = authenticationService.register(sessionIdentifier, shortenUrl, validateEmailBaseUrl, TestUtil.LOGIN_ADMIN, TestUtil.EMAIL, TestUtil.PASSWORD);
		assertNotNull(userIdentifier);

		final long bigBlind = 1000L;
		final String gameName = "testGame";
		final String firstPlayerName = "firstPlayer";
		final String secondPlayerName = "secondPlayer";
		final Long startCredits = 10000L;

		final PokerService pokerService = getPokerService();
		assertNotNull(pokerService.getGames());
		assertEquals(0, pokerService.getGames().size());
		assertNotNull(pokerService.getGamesNotRunning());
		assertEquals(0, pokerService.getGamesNotRunning().size());
		assertNotNull(pokerService.getGamesRunning());
		assertEquals(0, pokerService.getGamesRunning().size());

		final PokerGameDto game = new PokerGameDto();
		game.setBigBlind(bigBlind);
		game.setName(gameName);
		game.setStartCredits(10000l);
		final PokerGameIdentifier gameId = pokerService.createGame(game);
		assertNotNull(gameId);

		final PokerPlayerDto firstPlayerDto = new PokerPlayerDto();
		firstPlayerDto.setName(firstPlayerName);
		firstPlayerDto.setAmount(startCredits);
		firstPlayerDto.setOwners(new ArrayList<UserIdentifier>());

		final PokerPlayerIdentifier firstPlayerId = pokerService.createPlayer(sessionIdentifier, firstPlayerDto);
		pokerService.joinGame(gameId, firstPlayerId);

		final PokerPlayerDto secondPlayer = new PokerPlayerDto();
		secondPlayer.setName(secondPlayerName);
		secondPlayer.setAmount(startCredits);
		secondPlayer.setOwners(new ArrayList<UserIdentifier>());

		final PokerPlayerIdentifier secondPlayerId = pokerService.createPlayer(sessionIdentifier, secondPlayer);
		pokerService.joinGame(gameId, secondPlayerId);

		assertNotNull(pokerService.getGames());
		assertEquals(1, pokerService.getGames().size());
		assertNotNull(pokerService.getGamesNotRunning());
		assertEquals(1, pokerService.getGamesNotRunning().size());
		assertNotNull(pokerService.getGamesRunning());
		assertEquals(0, pokerService.getGamesRunning().size());

		pokerService.startGame(gameId);

		assertNotNull(pokerService.getGames());
		assertEquals(1, pokerService.getGames().size());
		assertNotNull(pokerService.getGamesNotRunning());
		assertEquals(0, pokerService.getGamesNotRunning().size());
		assertNotNull(pokerService.getGamesRunning());
		assertEquals(1, pokerService.getGamesRunning().size());

		/*
		 * Act
		 */
		final BundleContext bundleContext = getContext();
		assertNotNull(bundleContext);
		final ExtHttpServiceHelper extHttpService = new ExtHttpServiceHelper();
		final ServiceRegistration serviceRegistration = bundleContext.registerService(ExtHttpService.class.getName(), extHttpService, null);
		assertNotNull(serviceRegistration);

		final Servlet statusServlet = extHttpService.getServlet("/poker/game/status/json");
		assertNotNull(statusServlet);

		final HttpServletRequestMock request = new HttpServletRequestMock();
		request.setParameter("game_id", gameId.getId());
		request.setParameter("token", "P2huWY8zZWDd");
		final HttpServletResponseMock response = new HttpServletResponseMock();

		try {
			statusServlet.service(request, response);
			response.getWriter().flush();
		} catch (Exception e) {
			fail("unexpected exception: " + e.getClass().getName());
		}

		System.out.println("bier: " + new String(response.getContent()));

		/*
		 * Assert
		 */
		try {
			assertNotNull(response.getContent());
			assertTrue(response.getContent().length > 0);
			final JSONParser jsonParser = new JSONParserSimple();
			final Object object = jsonParser.parse(new String(response.getContent()));
			assertTrue(object instanceof JSONObject);
			final JSONObject jsonObject = (JSONObject) object;
			final Object gameNameObject = jsonObject.get("gameName");
			assertNotNull(gameNameObject);
			assertTrue(gameNameObject instanceof String);
			assertEquals(gameName, (String) gameNameObject);
		} catch (JSONParseException e) {
			fail("unexpected exception: " + e);
		}
	}
}
