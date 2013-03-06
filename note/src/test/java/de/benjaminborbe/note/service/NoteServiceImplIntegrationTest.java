package de.benjaminborbe.note.service;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.note.api.NoteService;
import de.benjaminborbe.note.guice.NoteModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class NoteServiceImplIntegrationTest {

	@Test
	public void testInject() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new NoteModulesMock());
		assertNotNull(injector.getInstance(NoteService.class));
	}
}
