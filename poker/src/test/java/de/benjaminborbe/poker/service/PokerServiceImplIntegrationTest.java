package de.benjaminborbe.poker.service;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.poker.api.PokerCardIdentifier;
import de.benjaminborbe.poker.api.PokerGame;
import de.benjaminborbe.poker.api.PokerGameIdentifier;
import de.benjaminborbe.poker.api.PokerPlayerIdentifier;
import de.benjaminborbe.poker.api.PokerService;
import de.benjaminborbe.poker.guice.PokerModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

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
		assertNotNull(service.getGames());
		assertEquals(0, service.getGames().size());
		{
			final PokerGameIdentifier gi = service.createGame("gameA", 100);
			assertNotNull(gi);
			assertNotNull(gi.getId());
		}
		assertNotNull(service.getGames());
		assertEquals(1, service.getGames().size());
		{
			final PokerGameIdentifier gi = service.createGame("gameB", 100);
			assertNotNull(gi);
			assertNotNull(gi.getId());
		}
		assertNotNull(service.getGames());
		assertEquals(2, service.getGames().size());
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
		final PokerService service = injector.getInstance(PokerService.class);
		final PokerGameIdentifier gameIdentifier = service.createGame("testGame", 100);
		assertNotNull(gameIdentifier);

		{
			final PokerGame game = service.getGame(gameIdentifier);
			assertNotNull(game);
			assertEquals(gameIdentifier, game.getId());
			assertEquals(Boolean.FALSE, game.getRunning());
			assertNull(game.getActivePlayer());
			assertEquals(new Long(0), game.getPot());
			assertEquals(new Long(50), game.getSmallBlind());
			assertEquals(new Long(100), game.getBigBlind());
		}

		assertNotNull(service.getPlayers(gameIdentifier));
		assertEquals(0, service.getPlayers(gameIdentifier).size());

		try {
			service.startGame(gameIdentifier);
			fail("ValidationException expected");
		}
		catch (final ValidationException e) {
			assertNotNull(e);
		}

		final PokerPlayerIdentifier playerIdentifierA = service.createPlayer("playerA");
		assertNotNull(playerIdentifierA);
		service.joinGame(gameIdentifier, playerIdentifierA);

		assertNotNull(service.getPlayers(gameIdentifier));
		assertEquals(1, service.getPlayers(gameIdentifier).size());

		try {
			service.startGame(gameIdentifier);
			fail("ValidationException expected");
		}
		catch (final ValidationException e) {
			assertNotNull(e);
		}

		final PokerPlayerIdentifier playerIdentifierB = service.createPlayer("playerB");
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
			assertNotNull(game.getActivePlayer());
			assertEquals(new Long(0), game.getPot());
			assertEquals(new Long(50), game.getSmallBlind());
			assertEquals(new Long(100), game.getBigBlind());

			final List<PokerPlayerIdentifier> players = game.getPlayers();
			assertNotNull(players);
			assertEquals(2, players.size());
			final List<PokerCardIdentifier> cards = game.getCards();
			assertNotNull(cards);
			assertEquals(52, cards.size());
		}
	}

	@Test
	public void testCards() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new PokerModulesMock());
		final PokerService service = injector.getInstance(PokerService.class);
		final PokerGameIdentifier gameIdentifier = service.createGame("testGame", 100);
		final PokerPlayerIdentifier playerIdentifierA = service.createPlayer("playerA");
		final PokerPlayerIdentifier playerIdentifierB = service.createPlayer("playerB");
		service.joinGame(gameIdentifier, playerIdentifierA);
		service.joinGame(gameIdentifier, playerIdentifierB);
		service.startGame(gameIdentifier);

		assertNotNull(service.getCards(playerIdentifierA));
		assertEquals(2, service.getCards(playerIdentifierA).size());

		assertNotNull(service.getCards(playerIdentifierB));
		assertEquals(2, service.getCards(playerIdentifierB).size());
	}
}
