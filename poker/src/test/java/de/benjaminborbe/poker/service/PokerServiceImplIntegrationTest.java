package de.benjaminborbe.poker.service;

import static org.junit.Assert.*;

import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.poker.api.PokerGameIdentifier;
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
			final PokerGameIdentifier gi = service.createGame("gameA");
			assertNotNull(gi);
			assertNotNull(gi.getId());
		}
		assertNotNull(service.getGames());
		assertEquals(1, service.getGames().size());
		{
			final PokerGameIdentifier gi = service.createGame("gameB");
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
}
