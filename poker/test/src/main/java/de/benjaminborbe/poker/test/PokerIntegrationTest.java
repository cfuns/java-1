package de.benjaminborbe.poker.test;

import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.poker.api.PokerGameDto;
import de.benjaminborbe.poker.api.PokerGameIdentifier;
import de.benjaminborbe.poker.api.PokerPlayerDto;
import de.benjaminborbe.poker.api.PokerPlayerIdentifier;
import de.benjaminborbe.poker.api.PokerService;
import de.benjaminborbe.poker.api.PokerServiceException;
import de.benjaminborbe.test.osgi.TestCaseOsgi;
import de.benjaminborbe.tools.osgi.mock.ExtHttpServiceMock;
import de.benjaminborbe.tools.url.UrlUtilImpl;
import org.apache.felix.http.api.ExtHttpService;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import javax.servlet.Servlet;
import java.io.IOException;
import java.util.ArrayList;

public class PokerIntegrationTest extends TestCaseOsgi {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testGetExtHttpService() {
		final BundleContext bundleContext = getContext();
		assertNotNull(bundleContext);
		final ExtHttpServiceMock extHttpService = new ExtHttpServiceMock(new UrlUtilImpl());
		assertNotNull(extHttpService);
		// zum start: keine Dienste registriert
		assertEquals(0, extHttpService.getRegisterFilterCallCounter());
		assertEquals(0, extHttpService.getRegisterServletCallCounter());
		assertEquals(0, extHttpService.getUnregisterFilterCallCounter());
		assertEquals(0, extHttpService.getUnregisterServletCallCounter());
		final ServiceRegistration serviceRegistration = bundleContext.registerService(ExtHttpService.class.getName(), extHttpService, null);
		assertNotNull(serviceRegistration);
		// nach start: Dienste vorhanden?
		assertTrue("no filters registered", extHttpService.getRegisterFilterCallCounter() > 0);
		assertTrue("no servlets registered.", extHttpService.getRegisterServletCallCounter() > 0);
		assertEquals(0, extHttpService.getUnregisterFilterCallCounter());
		assertEquals(0, extHttpService.getUnregisterServletCallCounter());

		// do unregister
		serviceRegistration.unregister();

		assertTrue("no servlets unregistered", extHttpService.getUnregisterServletCallCounter() > 0);
		assertEquals(extHttpService.getRegisterServletCallCounter(), extHttpService.getRegisterServletCallCounter());
		assertEquals(extHttpService.getRegisterFilterCallCounter(), extHttpService.getUnregisterFilterCallCounter());
	}

	public void testPokerService() {
		final PokerService service = getPokerService();
		assertNotNull(service);
		assertEquals("de.benjaminborbe.poker.service.PokerServiceImpl", service.getClass().getName());
	}

	private PokerService getPokerService() {
		final Object serviceObject = getServiceObject(PokerService.class.getName(), null);
		return (PokerService) serviceObject;
	}

	public void testSimpleGame() throws PokerServiceException, ValidationException {

		/*
		 * Arrange
		 */

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
		final PokerGameIdentifier gameId = pokerService.createGame(game);
		assertNotNull(gameId);

		final PokerPlayerDto firstPlayerDto = new PokerPlayerDto();
		firstPlayerDto.setName(firstPlayerName);
		firstPlayerDto.setAmount(startCredits);
		firstPlayerDto.setOwners(new ArrayList<UserIdentifier>());

		final PokerPlayerIdentifier firstPlayerId = pokerService.createPlayer(firstPlayerDto);
		pokerService.joinGame(gameId, firstPlayerId);

		final PokerPlayerDto secondPlayer = new PokerPlayerDto();
		secondPlayer.setName(secondPlayerName);
		secondPlayer.setAmount(startCredits);
		secondPlayer.setOwners(new ArrayList<UserIdentifier>());

		final PokerPlayerIdentifier secondPlayerId = pokerService.createPlayer(secondPlayer);
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

		HttpServletRequestMock request = new HttpServletRequestMock();
		HttpServletResponseMock response = new HttpServletResponseMock();

		try {
			statusServlet.service(request, response);
		} catch (Exception e) {
			e.printStackTrace();
			fail("unexpected exception: " + e.getClass().getName());
		}

		try {
			response.getWriter().flush();
		} catch (IOException e) {
			fail("unexpected exception: " + e.getClass().getName());
		}

		/*
		 * Assert
		 */
		System.err.println("bier:" + new String(response.getContent()));
	}

//	/poker/game/status/json
//	/poker/raise/json
//	/poker/call/json
//	/poker/fold/json
//	/poker/status/json
//	/poker/game/join
}
