package de.benjaminborbe.wiki.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Collection;

import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import de.benjaminborbe.wiki.api.WikiService;
import de.benjaminborbe.wiki.api.WikiServiceException;
import de.benjaminborbe.wiki.api.WikiSpaceIdentifier;
import de.benjaminborbe.wiki.guice.WikiModulesMock;

public class WikiServiceImplIntegrationTest {

	@Test
	public void testInject() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new WikiModulesMock());
		assertNotNull(injector.getInstance(WikiService.class));
		assertEquals(WikiServiceImpl.class, injector.getInstance(WikiService.class).getClass());
	}

	@Test
	public void testSpaces() throws WikiServiceException, ValidationException {
		final String spaceId = "testSpace";
		final String spaceTitle = "Test Space";
		final Injector injector = GuiceInjectorBuilder.getInjector(new WikiModulesMock());
		final WikiService wikiService = injector.getInstance(WikiService.class);

		{
			final Collection<WikiSpaceIdentifier> spaces = wikiService.getSpaceIdentifiers();
			assertNotNull(spaces);
			assertEquals(0, spaces.size());
		}

		final WikiSpaceIdentifier wikiSpaceIdentifier = wikiService.createSpace(spaceId, spaceTitle);
		assertNotNull(wikiSpaceIdentifier);

		{
			final Collection<WikiSpaceIdentifier> spaces = wikiService.getSpaceIdentifiers();
			assertNotNull(spaces);
			assertEquals(1, spaces.size());
		}

		wikiService.deleteSpace(wikiSpaceIdentifier);

		{
			final Collection<WikiSpaceIdentifier> spaces = wikiService.getSpaceIdentifiers();
			assertNotNull(spaces);
			assertEquals(0, spaces.size());
		}

	}
}
