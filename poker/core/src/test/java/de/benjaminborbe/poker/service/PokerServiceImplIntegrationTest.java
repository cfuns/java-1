package de.benjaminborbe.poker.service;

import com.google.inject.Injector;
import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.configuration.api.ConfigurationIdentifier;
import de.benjaminborbe.configuration.mock.ConfigurationServiceMock;
import de.benjaminborbe.poker.api.PokerCardIdentifier;
import de.benjaminborbe.poker.api.PokerGame;
import de.benjaminborbe.poker.api.PokerGameDto;
import de.benjaminborbe.poker.api.PokerGameIdentifier;
import de.benjaminborbe.poker.api.PokerPlayer;
import de.benjaminborbe.poker.api.PokerPlayerDto;
import de.benjaminborbe.poker.api.PokerPlayerIdentifier;
import de.benjaminborbe.poker.api.PokerService;
import de.benjaminborbe.poker.config.PokerCoreConfig;
import de.benjaminborbe.poker.game.PokerGameBean;
import de.benjaminborbe.poker.game.PokerGameDao;
import de.benjaminborbe.poker.guice.PokerModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import org.easymock.EasyMock;
import org.junit.Ignore;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class PokerServiceImplIntegrationTest {

	@Test
	public void testInject() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new PokerModulesMock());
		assertEquals(PokerServiceImpl.class, injector.getInstance(PokerService.class).getClass());
	}

	@Test
	public void testCreateGame() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new PokerModulesMock());
		final PokerService service = injector.getInstance(PokerService.class);
		assertNotNull(service.getGameIdentifiers());
		assertEquals(0, service.getGameIdentifiers().size());
		{
			final PokerGameIdentifier gi = service.createGame(createGame("gameA", 100, 10000));
			assertNotNull(gi);
			assertNotNull(gi.getId());
		}
		assertNotNull(service.getGameIdentifiers());
		assertEquals(1, service.getGameIdentifiers().size());
		{
			final PokerGameIdentifier gi = service.createGame(createGame("gameB", 100, 10000));
			assertNotNull(gi);
			assertNotNull(gi.getId());
		}
		assertNotNull(service.getGameIdentifiers());
		assertEquals(2, service.getGameIdentifiers().size());
	}

	@Test
	public void testScoreUpdateOnGameStart() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new PokerModulesMock());
		final PokerService service = injector.getInstance(PokerService.class);

		final String sessionId = "sid123";

		final HttpSession session = EasyMock.createMock(HttpSession.class);
		EasyMock.expect(session.getId()).andReturn(sessionId);
		EasyMock.replay(session);

		final HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
		EasyMock.expect(request.getSession()).andReturn(session);
		EasyMock.replay(request);

		final AuthenticationService authenticationService = injector.getInstance(AuthenticationService.class);
		final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);

		final PokerGameIdentifier gameIdentifier = service.createGame(createGame("gameA", 100, 10000));
		assertNotNull(gameIdentifier);

		final PokerPlayerIdentifier playerIdentifierA = service.createPlayer(sessionIdentifier, createPlayerDto("playerA", 10000));
		assertNotNull(playerIdentifierA);
		service.joinGame(gameIdentifier, playerIdentifierA);

		final PokerPlayerIdentifier playerIdentifierB = service.createPlayer(sessionIdentifier, createPlayerDto("playerB", 10000));
		assertNotNull(playerIdentifierB);
		service.joinGame(gameIdentifier, playerIdentifierB);

		final PokerPlayerIdentifier playerIdentifierC = service.createPlayer(sessionIdentifier, createPlayerDto("playerC", 10000));
		assertNotNull(playerIdentifierC);
		service.joinGame(gameIdentifier, playerIdentifierC);

		assertThat(service.getGame(gameIdentifier).getScore(), is(0l));
		assertThat(service.getPlayer(playerIdentifierA).getScore(), is(0l));
		assertThat(service.getPlayer(playerIdentifierB).getScore(), is(0l));
		assertThat(service.getPlayer(playerIdentifierC).getScore(), is(0l));

		service.startGame(gameIdentifier);

		assertThat(service.getGame(gameIdentifier).getScore(), is(3L));
		assertThat(service.getPlayer(playerIdentifierA).getScore(), is(-1L));
		assertThat(service.getPlayer(playerIdentifierB).getScore(), is(-1L));
		assertThat(service.getPlayer(playerIdentifierC).getScore(), is(-1L));

		service.stopGame(gameIdentifier);

		assertThat(service.getGame(gameIdentifier).getScore(), is(0l));
		assertThat(service.getPlayer(playerIdentifierA).getScore(), is(0l));
		assertThat(service.getPlayer(playerIdentifierB).getScore(), is(0l));
		assertThat(service.getPlayer(playerIdentifierC).getScore(), is(0l));
	}

	@Test
	public void testNewGameHasScoreZero() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new PokerModulesMock());
		final PokerService service = injector.getInstance(PokerService.class);

		final String sessionId = "sid123";

		final HttpSession session = EasyMock.createMock(HttpSession.class);
		EasyMock.expect(session.getId()).andReturn(sessionId);
		EasyMock.replay(session);

		final HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
		EasyMock.expect(request.getSession()).andReturn(session);
		EasyMock.replay(request);

		final AuthenticationService authenticationService = injector.getInstance(AuthenticationService.class);
		final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);

		final PokerGameIdentifier gameIdentifier = service.createGame(createGame("gameA", 100, 10000));
		assertNotNull(gameIdentifier);

		final PokerGame game = service.getGame(gameIdentifier);
		assertThat(game, is(notNullValue()));
		assertThat(game.getScore(), is(0l));
	}

	@Test
	public void testNewPlayerHasScoreZero() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new PokerModulesMock());
		final PokerService service = injector.getInstance(PokerService.class);

		final String sessionId = "sid123";

		final HttpSession session = EasyMock.createMock(HttpSession.class);
		EasyMock.expect(session.getId()).andReturn(sessionId);
		EasyMock.replay(session);

		final HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
		EasyMock.expect(request.getSession()).andReturn(session);
		EasyMock.replay(request);

		final AuthenticationService authenticationService = injector.getInstance(AuthenticationService.class);
		final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);

		final PokerPlayerIdentifier playerIdentifier = service.createPlayer(sessionIdentifier, createPlayerDto("playerA", 10000));
		assertNotNull(playerIdentifier);

		final PokerPlayer player = service.getPlayer(playerIdentifier);
		assertThat(player, is(notNullValue()));
		assertThat(player.getScore(), is(0l));
	}

	@Test
	public void testDelete() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new PokerModulesMock());

		final String sessionId = "sid123";

		final HttpSession session = EasyMock.createMock(HttpSession.class);
		EasyMock.expect(session.getId()).andReturn(sessionId);
		EasyMock.replay(session);

		final HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
		EasyMock.expect(request.getSession()).andReturn(session);
		EasyMock.replay(request);

		final AuthenticationService authenticationService = injector.getInstance(AuthenticationService.class);
		final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);

		final PokerService service = injector.getInstance(PokerService.class);
		assertNotNull(service.getGameIdentifiers());
		assertEquals(0, service.getGameIdentifiers().size());
		final PokerGameIdentifier gameIdentifier = service.createGame(createGame("game", 100, 10000));
		assertNotNull(gameIdentifier);
		assertNotNull(gameIdentifier.getId());
		assertNotNull(service.getGameIdentifiers());
		assertEquals(1, service.getGameIdentifiers().size());

		final PokerPlayerIdentifier playerIdentifier = service.createPlayer(sessionIdentifier, createPlayerDto("player", 10000));
		service.joinGame(gameIdentifier, playerIdentifier);

		{
			final PokerPlayer player = service.getPlayer(playerIdentifier);
			assertNotNull(player);
			assertNotNull(player.getGame());
			assertEquals(gameIdentifier, player.getGame());
		}

		service.deleteGame(gameIdentifier);
		assertEquals(0, service.getGameIdentifiers().size());

		{
			final PokerPlayer player = service.getPlayer(playerIdentifier);
			assertNotNull(player);
			assertNull(player.getGame());
		}

	}

	@Test
	public void testCreatePlayerIdentifier() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new PokerModulesMock());
		final PokerService service = injector.getInstance(PokerService.class);
		assertNotNull(service.createPlayerIdentifier("1337"));
		assertEquals("1337", service.createPlayerIdentifier("1337").getId());
		assertNull(service.createPlayerIdentifier(null));
	}

	@Test
	public void testCreateGameIdentifier() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new PokerModulesMock());
		final PokerService service = injector.getInstance(PokerService.class);
		assertNotNull(service.createGameIdentifier("1337"));
		assertEquals("1337", service.createGameIdentifier("1337").getId());
		assertNull(service.createGameIdentifier(null));
	}

	@Test
	public void testStartGame() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new PokerModulesMock());

		final String sessionId = "sid123";

		final HttpSession session = EasyMock.createMock(HttpSession.class);
		EasyMock.expect(session.getId()).andReturn(sessionId);
		EasyMock.replay(session);

		final HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
		EasyMock.expect(request.getSession()).andReturn(session);
		EasyMock.replay(request);

		final AuthenticationService authenticationService = injector.getInstance(AuthenticationService.class);
		final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);

		final PokerService service = injector.getInstance(PokerService.class);
		final PokerGameIdentifier gameIdentifier = service.createGame(createGame("testGame", 100, 10000));
		assertNotNull(gameIdentifier);

		{
			final PokerGame game = service.getGame(gameIdentifier);
			assertNotNull(game);
			assertEquals(gameIdentifier, game.getId());
			assertEquals(Boolean.FALSE, game.getRunning());
			assertEquals(new Long(0), game.getPot());
			assertEquals(new Long(50), game.getSmallBlind());
			assertEquals(new Long(100), game.getBigBlind());

			assertNull(service.getActivePlayer(gameIdentifier));
		}

		assertNotNull(service.getPlayers(gameIdentifier));
		assertEquals(0, service.getPlayers(gameIdentifier).size());

		try {
			service.startGame(gameIdentifier);
			fail("ValidationException expected");
		} catch (final ValidationException e) {
			assertNotNull(e);
		}

		final PokerPlayerIdentifier playerIdentifierA = service.createPlayer(sessionIdentifier, createPlayerDto("playerA", 10000));
		assertNotNull(playerIdentifierA);
		service.joinGame(gameIdentifier, playerIdentifierA);

		assertNotNull(service.getPlayers(gameIdentifier));
		assertEquals(1, service.getPlayers(gameIdentifier).size());

		try {
			service.startGame(gameIdentifier);
			fail("ValidationException expected");
		} catch (final ValidationException e) {
			assertNotNull(e);
		}

		final PokerPlayerIdentifier playerIdentifierB = service.createPlayer(sessionIdentifier, createPlayerDto("playerB", 10000));
		assertNotNull(playerIdentifierB);
		service.joinGame(gameIdentifier, playerIdentifierB);

		assertNotNull(service.getPlayers(gameIdentifier));
		assertEquals(2, service.getPlayers(gameIdentifier).size());

		service.startGame(gameIdentifier);

		{
			final PokerGame game = service.getGame(gameIdentifier);
			assertNotNull(game);
			assertEquals(gameIdentifier, game.getId());
			assertEquals(Boolean.TRUE, game.getRunning());
			assertEquals(new Long(50), game.getSmallBlind());
			assertEquals(new Long(100), game.getBigBlind());
			assertEquals(new Long(100), game.getBet());
			assertEquals(new Long(150), game.getPot());

			assertNotNull(service.getActivePlayer(gameIdentifier));

			final List<PokerPlayerIdentifier> players = game.getPlayers();
			assertNotNull(players);
			assertEquals(2, players.size());
			final List<PokerCardIdentifier> cards = game.getCards();
			assertNotNull(cards);
			assertEquals(52, cards.size());
		}
	}

	@Test
	public void testHandCards() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new PokerModulesMock());

		final String sessionId = "sid123";

		final HttpSession session = EasyMock.createMock(HttpSession.class);
		EasyMock.expect(session.getId()).andReturn(sessionId);
		EasyMock.replay(session);

		final HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
		EasyMock.expect(request.getSession()).andReturn(session);
		EasyMock.replay(request);

		final AuthenticationService authenticationService = injector.getInstance(AuthenticationService.class);
		final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);

		final PokerService service = injector.getInstance(PokerService.class);
		final PokerGameIdentifier gameIdentifier = service.createGame(createGame("testGame", 100, 10000));
		final PokerPlayerIdentifier playerIdentifierA = service.createPlayer(sessionIdentifier, createPlayerDto("playerA", 10000));
		final PokerPlayerIdentifier playerIdentifierB = service.createPlayer(sessionIdentifier, createPlayerDto("playerB", 10000));
		service.joinGame(gameIdentifier, playerIdentifierA);
		service.joinGame(gameIdentifier, playerIdentifierB);
		service.startGame(gameIdentifier);

		assertNotNull(service.getHandCards(playerIdentifierA));
		assertEquals(2, service.getHandCards(playerIdentifierA).size());

		assertNotNull(service.getHandCards(playerIdentifierB));
		assertEquals(2, service.getHandCards(playerIdentifierB).size());

		assertNotNull(service.getBoardCards(gameIdentifier));
		assertEquals(0, service.getBoardCards(gameIdentifier).size());
	}

	@Ignore("todo")
	@Test
	public void testBoardCards() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new PokerModulesMock());

		final String sessionId = "sid123";

		final HttpSession session = EasyMock.createMock(HttpSession.class);
		EasyMock.expect(session.getId()).andReturn(sessionId);
		EasyMock.replay(session);

		final HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
		EasyMock.expect(request.getSession()).andReturn(session);
		EasyMock.replay(request);

		final AuthenticationService authenticationService = injector.getInstance(AuthenticationService.class);
		final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);

		final PokerService service = injector.getInstance(PokerService.class);
		final PokerGameIdentifier gameIdentifier = service.createGame(createGame("testGame", 100, 10000));
		final PokerPlayerIdentifier playerIdentifierA = service.createPlayer(sessionIdentifier, createPlayerDto("playerA", 10000));
		final PokerPlayerIdentifier playerIdentifierB = service.createPlayer(sessionIdentifier, createPlayerDto("playerB", 10000));
		service.joinGame(gameIdentifier, playerIdentifierA);
		service.joinGame(gameIdentifier, playerIdentifierB);
		service.startGame(gameIdentifier);

		assertNotNull(service.getBoardCards(gameIdentifier));
		assertEquals(0, service.getBoardCards(gameIdentifier).size());
		{
			final PokerPlayerIdentifier player = service.getActivePlayer(gameIdentifier);
			service.call(gameIdentifier, player);
		}
		{
			final PokerPlayerIdentifier player = service.getActivePlayer(gameIdentifier);
			service.call(gameIdentifier, player);
		}
		assertNotNull(service.getBoardCards(gameIdentifier));
		assertEquals(3, service.getBoardCards(gameIdentifier).size());
		{
			final PokerPlayerIdentifier player = service.getActivePlayer(gameIdentifier);
			service.call(gameIdentifier, player);
		}
		{
			final PokerPlayerIdentifier player = service.getActivePlayer(gameIdentifier);
			service.call(gameIdentifier, player);
		}
		assertNotNull(service.getBoardCards(gameIdentifier));
		assertEquals(4, service.getBoardCards(gameIdentifier).size());
		{
			final PokerPlayerIdentifier player = service.getActivePlayer(gameIdentifier);
			service.call(gameIdentifier, player);
		}
		{
			final PokerPlayerIdentifier player = service.getActivePlayer(gameIdentifier);
			service.call(gameIdentifier, player);
		}
		assertNotNull(service.getBoardCards(gameIdentifier));
		assertEquals(5, service.getBoardCards(gameIdentifier).size());
	}

	@Test
	public void testActivePlayerCall() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new PokerModulesMock());

		final String sessionId = "sid123";

		final HttpSession session = EasyMock.createMock(HttpSession.class);
		EasyMock.expect(session.getId()).andReturn(sessionId);
		EasyMock.replay(session);

		final HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
		EasyMock.expect(request.getSession()).andReturn(session);
		EasyMock.replay(request);

		final AuthenticationService authenticationService = injector.getInstance(AuthenticationService.class);
		final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);

		final PokerService service = injector.getInstance(PokerService.class);
		final PokerGameIdentifier gameIdentifier = service.createGame(createGame("testGame", 100, 10000));
		final PokerPlayerIdentifier playerIdentifierA = service.createPlayer(sessionIdentifier, createPlayerDto("playerA", 10000));
		final PokerPlayerIdentifier playerIdentifierB = service.createPlayer(sessionIdentifier, createPlayerDto("playerB", 10000));
		service.joinGame(gameIdentifier, playerIdentifierA);
		service.joinGame(gameIdentifier, playerIdentifierB);
		service.startGame(gameIdentifier);

		final PokerPlayerIdentifier activePlayerA = service.getActivePlayer(gameIdentifier);
		assertNotNull(activePlayerA);

		service.call(gameIdentifier, activePlayerA);

		final PokerPlayerIdentifier activePlayerB = service.getActivePlayer(gameIdentifier);
		assertNotNull(activePlayerB);

		assertFalse(activePlayerA.equals(activePlayerB));
	}

	@Test
	public void testActivePlayerRaise() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new PokerModulesMock());

		final String sessionId = "sid123";

		final HttpSession session = EasyMock.createMock(HttpSession.class);
		EasyMock.expect(session.getId()).andReturn(sessionId);
		EasyMock.replay(session);

		final HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
		EasyMock.expect(request.getSession()).andReturn(session);
		EasyMock.replay(request);

		final AuthenticationService authenticationService = injector.getInstance(AuthenticationService.class);
		final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);

		final PokerService service = injector.getInstance(PokerService.class);
		final PokerGameIdentifier gameIdentifier = service.createGame(createGame("testGame", 100, 10000));
		final PokerPlayerIdentifier playerIdentifierA = service.createPlayer(sessionIdentifier, createPlayerDto("playerA", 10000));
		final PokerPlayerIdentifier playerIdentifierB = service.createPlayer(sessionIdentifier, createPlayerDto("playerB", 10000));
		service.joinGame(gameIdentifier, playerIdentifierA);
		service.joinGame(gameIdentifier, playerIdentifierB);
		service.startGame(gameIdentifier);

		final PokerPlayerIdentifier activePlayerA = service.getActivePlayer(gameIdentifier);
		assertNotNull(activePlayerA);

		service.raise(gameIdentifier, activePlayerA, 500l);

		final PokerPlayerIdentifier activePlayerB = service.getActivePlayer(gameIdentifier);
		assertNotNull(activePlayerB);

		assertFalse(activePlayerA.equals(activePlayerB));
	}

	@Test
	public void testCall() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new PokerModulesMock());

		final String sessionId = "sid123";

		final HttpSession session = EasyMock.createMock(HttpSession.class);
		EasyMock.expect(session.getId()).andReturn(sessionId);
		EasyMock.replay(session);

		final HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
		EasyMock.expect(request.getSession()).andReturn(session);
		EasyMock.replay(request);

		final AuthenticationService authenticationService = injector.getInstance(AuthenticationService.class);
		final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);

		final PokerService service = injector.getInstance(PokerService.class);
		final PokerGameIdentifier gameIdentifier = service.createGame(createGame("testGame", 100, 10000));
		final PokerPlayerIdentifier playerIdentifierA = service.createPlayer(sessionIdentifier, createPlayerDto("playerA", 10000));
		final PokerPlayerIdentifier playerIdentifierB = service.createPlayer(sessionIdentifier, createPlayerDto("playerB", 10000));
		final PokerPlayerIdentifier playerIdentifierC = service.createPlayer(sessionIdentifier, createPlayerDto("playerC", 10000));
		final PokerPlayerIdentifier playerIdentifierD = service.createPlayer(sessionIdentifier, createPlayerDto("playerD", 10000));
		service.joinGame(gameIdentifier, playerIdentifierA);
		service.joinGame(gameIdentifier, playerIdentifierB);
		service.joinGame(gameIdentifier, playerIdentifierC);
		service.joinGame(gameIdentifier, playerIdentifierD);
		service.startGame(gameIdentifier);

		{
			final PokerGame game = service.getGame(gameIdentifier);
			assertEquals(new Long(100), game.getBet());
			assertEquals(new Long(150), game.getPot());
		}

		{
			final PokerPlayerIdentifier activePlayer = service.getActivePlayer(gameIdentifier);
			assertNotNull(activePlayer);
			service.call(gameIdentifier, activePlayer);
		}

		{
			final PokerGame game = service.getGame(gameIdentifier);
			assertEquals(new Long(100), game.getBet());
			assertEquals(new Long(250), game.getPot());
		}

		{
			final PokerPlayerIdentifier activePlayer = service.getActivePlayer(gameIdentifier);
			assertNotNull(activePlayer);
			service.call(gameIdentifier, activePlayer);
		}

		{
			final PokerGame game = service.getGame(gameIdentifier);
			assertEquals(new Long(100), game.getBet());
			assertEquals(new Long(350), game.getPot());
		}

		{
			final PokerPlayerIdentifier activePlayer = service.getActivePlayer(gameIdentifier);
			assertNotNull(activePlayer);
			service.call(gameIdentifier, activePlayer);
		}

		{
			final PokerGame game = service.getGame(gameIdentifier);
			assertEquals(new Long(100), game.getBet());
			assertEquals(new Long(400), game.getPot());
		}
	}

	@Test
	public void testRaise() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new PokerModulesMock());

		final String sessionId = "sid123";

		final HttpSession session = EasyMock.createMock(HttpSession.class);
		EasyMock.expect(session.getId()).andReturn(sessionId);
		EasyMock.replay(session);

		final HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
		EasyMock.expect(request.getSession()).andReturn(session);
		EasyMock.replay(request);

		final AuthenticationService authenticationService = injector.getInstance(AuthenticationService.class);
		final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);

		final PokerService service = injector.getInstance(PokerService.class);
		final PokerGameIdentifier gameIdentifier = service.createGame(createGame("testGame", 100, 10000));
		final PokerPlayerIdentifier playerIdentifierA = service.createPlayer(sessionIdentifier, createPlayerDto("playerA", 10000));
		final PokerPlayerIdentifier playerIdentifierB = service.createPlayer(sessionIdentifier, createPlayerDto("playerB", 10000));
		final PokerPlayerIdentifier playerIdentifierC = service.createPlayer(sessionIdentifier, createPlayerDto("playerC", 10000));
		final PokerPlayerIdentifier playerIdentifierD = service.createPlayer(sessionIdentifier, createPlayerDto("playerD", 10000));
		service.joinGame(gameIdentifier, playerIdentifierA);
		service.joinGame(gameIdentifier, playerIdentifierB);
		service.joinGame(gameIdentifier, playerIdentifierC);
		service.joinGame(gameIdentifier, playerIdentifierD);
		service.startGame(gameIdentifier);

		{
			final PokerGame game = service.getGame(gameIdentifier);
			assertEquals(new Long(100), game.getBet());
			assertEquals(new Long(150), game.getPot());
		}

		{
			final PokerPlayerIdentifier activePlayer = service.getActivePlayer(gameIdentifier);
			assertNotNull(activePlayer);
			service.raise(gameIdentifier, activePlayer, 200);
		}

		{
			final PokerGame game = service.getGame(gameIdentifier);
			assertEquals(new Long(200), game.getBet());
			assertEquals(new Long(350), game.getPot());
		}

		{
			final PokerPlayerIdentifier activePlayer = service.getActivePlayer(gameIdentifier);
			assertNotNull(activePlayer);
			service.call(gameIdentifier, activePlayer);
		}

		{
			final PokerGame game = service.getGame(gameIdentifier);
			assertEquals(new Long(200), game.getBet());
			assertEquals(new Long(550), game.getPot());
		}

		{
			final PokerPlayerIdentifier activePlayer = service.getActivePlayer(gameIdentifier);
			assertNotNull(activePlayer);
			service.call(gameIdentifier, activePlayer);
		}

		{
			final PokerGame game = service.getGame(gameIdentifier);
			assertEquals(new Long(200), game.getBet());
			assertEquals(new Long(700), game.getPot());
		}

		{
			final PokerPlayerIdentifier activePlayer = service.getActivePlayer(gameIdentifier);
			assertNotNull(activePlayer);
			service.call(gameIdentifier, activePlayer);
		}

		{
			final PokerGame game = service.getGame(gameIdentifier);
			assertEquals(new Long(200), game.getBet());
			assertEquals(new Long(800), game.getPot());
		}
	}

	@Test
	public void testRaiseLimits() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new PokerModulesMock());

		final String sessionId = "sid123";

		final HttpSession session = EasyMock.createMock(HttpSession.class);
		EasyMock.expect(session.getId()).andReturn(sessionId);
		EasyMock.replay(session);

		final HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
		EasyMock.expect(request.getSession()).andReturn(session);
		EasyMock.replay(request);

		final AuthenticationService authenticationService = injector.getInstance(AuthenticationService.class);
		final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);

		final PokerService service = injector.getInstance(PokerService.class);
		final PokerGameIdentifier gameIdentifier = service.createGame(createGame("testGame", 100, 10000));
		final PokerPlayerIdentifier playerIdentifierA = service.createPlayer(sessionIdentifier, createPlayerDto("playerA", 10000));
		final PokerPlayerIdentifier playerIdentifierB = service.createPlayer(sessionIdentifier, createPlayerDto("playerB", 10000));
		final PokerPlayerIdentifier playerIdentifierC = service.createPlayer(sessionIdentifier, createPlayerDto("playerC", 10000));
		final PokerPlayerIdentifier playerIdentifierD = service.createPlayer(sessionIdentifier, createPlayerDto("playerD", 10000));
		service.joinGame(gameIdentifier, playerIdentifierA);
		service.joinGame(gameIdentifier, playerIdentifierB);
		service.joinGame(gameIdentifier, playerIdentifierC);
		service.joinGame(gameIdentifier, playerIdentifierD);
		service.startGame(gameIdentifier);

		{
			final PokerGame game = service.getGame(gameIdentifier);
			assertEquals(new Long(100), game.getBet());
			assertEquals(new Long(150), game.getPot());
		}

		{
			final PokerPlayerIdentifier activePlayer = service.getActivePlayer(gameIdentifier);
			assertNotNull(activePlayer);
			try {
				service.raise(gameIdentifier, activePlayer, 180);
				fail("raise should fail, min raise 2xBet");
			} catch (final ValidationException e) {
			}
		}

		{
			final PokerPlayerIdentifier activePlayer = service.getActivePlayer(gameIdentifier);
			assertNotNull(activePlayer);
			service.raise(gameIdentifier, activePlayer, 200);
		}

		{
			final PokerGame game = service.getGame(gameIdentifier);
			assertEquals(new Long(200), game.getBet());
			assertEquals(new Long(350), game.getPot());
		}

		{
			final PokerPlayerIdentifier activePlayer = service.getActivePlayer(gameIdentifier);
			assertNotNull(activePlayer);
			service.call(gameIdentifier, activePlayer);
		}

		{
			final PokerGame game = service.getGame(gameIdentifier);
			assertEquals(new Long(200), game.getBet());
			assertEquals(new Long(550), game.getPot());
		}

		{
			final PokerPlayerIdentifier activePlayer = service.getActivePlayer(gameIdentifier);
			assertNotNull(activePlayer);
			try {
				service.raise(gameIdentifier, activePlayer, 2100);
				fail("raise should fail, max raise 2xBet");
			} catch (final ValidationException e) {
			}
		}
		{
			final PokerPlayerIdentifier activePlayer = service.getActivePlayer(gameIdentifier);
			assertNotNull(activePlayer);
			service.raise(gameIdentifier, activePlayer, 2000);
		}

		{
			final PokerGame game = service.getGame(gameIdentifier);
			assertEquals(new Long(2000), game.getBet());
			assertEquals(new Long(2500), game.getPot());
		}

		{
			final PokerPlayerIdentifier activePlayer = service.getActivePlayer(gameIdentifier);
			assertNotNull(activePlayer);
			service.call(gameIdentifier, activePlayer);
		}

		{
			final PokerGame game = service.getGame(gameIdentifier);
			assertEquals(new Long(2000), game.getBet());
			assertEquals(new Long(4400), game.getPot());
		}
	}

	@Test
	public void testMaxBid() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new PokerModulesMock());

		final String sessionId = "sid123";

		final HttpSession session = EasyMock.createMock(HttpSession.class);
		EasyMock.expect(session.getId()).andReturn(sessionId);
		EasyMock.replay(session);

		final HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
		EasyMock.expect(request.getSession()).andReturn(session);
		EasyMock.replay(request);

		final AuthenticationService authenticationService = injector.getInstance(AuthenticationService.class);
		final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);

		final ConfigurationServiceMock configurationServiceMock = injector.getInstance(ConfigurationServiceMock.class);
		final PokerCoreConfig config = injector.getInstance(PokerCoreConfig.class);
		final PokerService service = injector.getInstance(PokerService.class);

		final PokerGameIdentifier gameIdentifier = service.createGame(createGame("testGame", 100, 10000));
		final PokerPlayerIdentifier playerIdentifierA = service.createPlayer(sessionIdentifier, createPlayerDto("playerA", 10000));
		final PokerPlayerIdentifier playerIdentifierB = service.createPlayer(sessionIdentifier, createPlayerDto("playerB", 10000));
		service.joinGame(gameIdentifier, playerIdentifierA);
		service.joinGame(gameIdentifier, playerIdentifierB);
		service.startGame(gameIdentifier);

		configurationServiceMock.setConfigurationValue(new ConfigurationIdentifier("PokerMaxBid"), "500");
		assertEquals(500l, config.getMaxBid());

		{
			final PokerPlayerIdentifier activePlayer = service.getActivePlayer(gameIdentifier);
			assertNotNull(activePlayer);
			service.raise(gameIdentifier, activePlayer, 500l);
		}

		{
			final PokerPlayerIdentifier activePlayer = service.getActivePlayer(gameIdentifier);
			assertNotNull(activePlayer);
			try {
				service.raise(gameIdentifier, activePlayer, 501l);
				fail("ValidationException expected");
			} catch (final ValidationException e) {
				assertNotNull(e);
			}
		}

		configurationServiceMock.setConfigurationValue(new ConfigurationIdentifier("PokerMaxBid"), "0");

		{
			final PokerPlayerIdentifier activePlayer = service.getActivePlayer(gameIdentifier);
			assertNotNull(activePlayer);
			service.raise(gameIdentifier, activePlayer, 1000l);
		}
	}

	@Test
	public void testBidNegativeCredits() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new PokerModulesMock());

		final String sessionId = "sid123";

		final HttpSession session = EasyMock.createMock(HttpSession.class);
		EasyMock.expect(session.getId()).andReturn(sessionId);
		EasyMock.replay(session);

		final HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
		EasyMock.expect(request.getSession()).andReturn(session);
		EasyMock.replay(request);

		final AuthenticationService authenticationService = injector.getInstance(AuthenticationService.class);
		final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);

		final PokerService service = injector.getInstance(PokerService.class);
		final ConfigurationServiceMock configurationServiceMock = injector.getInstance(ConfigurationServiceMock.class);
		configurationServiceMock.setConfigurationValue(new ConfigurationIdentifier("PokerMaxRaiseFactor"), "1000");

		final PokerGameIdentifier gameIdentifier = service.createGame(createGame("testGame", 100, 10000));
		final int startCredits = 10000;
		final PokerPlayerIdentifier playerIdentifierA = service.createPlayer(sessionIdentifier, createPlayerDto("playerA", startCredits));
		final PokerPlayerIdentifier playerIdentifierB = service.createPlayer(sessionIdentifier, createPlayerDto("playerB", startCredits));
		service.joinGame(gameIdentifier, playerIdentifierA);
		service.joinGame(gameIdentifier, playerIdentifierB);
		service.startGame(gameIdentifier);

		{
			final PokerGame game = service.getGame(gameIdentifier);
			assertNotNull(game);
			assertFalse(game.getCreditsNegativeAllowed());
		}

		{
			final PokerPlayerIdentifier activePlayer = service.getActivePlayer(gameIdentifier);
			assertNotNull(activePlayer);
			service.raise(gameIdentifier, activePlayer, startCredits * 2);
		}

		{
			final PokerGame game = service.getGame(gameIdentifier);
			assertNotNull(game);
			assertEquals(new Long(startCredits), game.getBet());
		}

		{
			final PokerGameDao pokerGameDao = injector.getInstance(PokerGameDao.class);
			final PokerGameBean game = pokerGameDao.load(gameIdentifier);
			game.setCreditsNegativeAllowed(true);
			pokerGameDao.save(game);
		}

		{
			final PokerGame game = service.getGame(gameIdentifier);
			assertNotNull(game);
			assertTrue(game.getCreditsNegativeAllowed());
		}

		{
			final PokerPlayerIdentifier activePlayer = service.getActivePlayer(gameIdentifier);
			assertNotNull(activePlayer);
			service.raise(gameIdentifier, activePlayer, startCredits * 2);
		}

		{
			final PokerGame game = service.getGame(gameIdentifier);
			assertNotNull(game);
			assertEquals(new Long(startCredits * 2), game.getBet());
		}
	}

	private PokerGameDto createGame(final String name, final long bigBlind, final long startCredits) {
		final PokerGameDto dto = new PokerGameDto();
		dto.setName(name);
		dto.setBigBlind(bigBlind);
		dto.setStartCredits(startCredits);
		return dto;
	}

	private PokerPlayerDto createPlayerDto(final String name, final long credits) {
		final PokerPlayerDto dto = new PokerPlayerDto();
		dto.setName(name);
		dto.setAmount(credits);
		return dto;
	}

	@Test
	public void testStartCreditGrantedOnStart() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new PokerModulesMock());
		final PokerService service = injector.getInstance(PokerService.class);

		final String sessionId = "sid123";

		final HttpSession session = EasyMock.createMock(HttpSession.class);
		EasyMock.expect(session.getId()).andReturn(sessionId);
		EasyMock.replay(session);

		final HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
		EasyMock.expect(request.getSession()).andReturn(session);
		EasyMock.replay(request);

		final AuthenticationService authenticationService = injector.getInstance(AuthenticationService.class);
		final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);

		final PokerGameIdentifier gameIdentifier = service.createGame(createGame("gameA", 100, 10000));
		assertNotNull(gameIdentifier);

		final PokerPlayerIdentifier playerIdentifierA = service.createPlayer(sessionIdentifier, createPlayerDto("playerA", 0));
		assertNotNull(playerIdentifierA);
		service.joinGame(gameIdentifier, playerIdentifierA);

		final PokerPlayerIdentifier playerIdentifierB = service.createPlayer(sessionIdentifier, createPlayerDto("playerB", 0));
		assertNotNull(playerIdentifierB);
		service.joinGame(gameIdentifier, playerIdentifierB);

		final PokerPlayerIdentifier playerIdentifierC = service.createPlayer(sessionIdentifier, createPlayerDto("playerC", 0));
		assertNotNull(playerIdentifierC);
		service.joinGame(gameIdentifier, playerIdentifierC);

		assertThat(service.getGame(gameIdentifier).getStartCredits(), is(10000l));
		assertThat(service.getPlayer(playerIdentifierA).getAmount(), is(0l));
		assertThat(service.getPlayer(playerIdentifierB).getAmount(), is(0l));
		assertThat(service.getPlayer(playerIdentifierC).getAmount(), is(0l));

		service.startGame(gameIdentifier);

		assertThat(service.getGame(gameIdentifier).getStartCredits(), is(10000l));
		assertThat(service.getPlayer(playerIdentifierA).getAmount() > 0, is(true));
		assertThat(service.getPlayer(playerIdentifierB).getAmount() > 0, is(true));
		assertThat(service.getPlayer(playerIdentifierC).getAmount() > 0, is(true));
	}
}
