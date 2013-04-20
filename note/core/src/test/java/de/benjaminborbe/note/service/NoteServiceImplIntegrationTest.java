package de.benjaminborbe.note.service;

import com.google.inject.Injector;
import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authentication.mock.AuthenticationServiceMock;
import de.benjaminborbe.note.api.Note;
import de.benjaminborbe.note.api.NoteDto;
import de.benjaminborbe.note.api.NoteIdentifier;
import de.benjaminborbe.note.api.NoteService;
import de.benjaminborbe.note.guice.NoteModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

public class NoteServiceImplIntegrationTest {

	@Test
	public void testInject() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new NoteModulesMock());
		assertNotNull(injector.getInstance(NoteService.class));
		final AuthenticationService authenticationServiceMock = injector.getInstance(AuthenticationServiceMock.class);
		assertThat(authenticationServiceMock, is(notNullValue()));
		final AuthenticationService authenticationService = injector.getInstance(AuthenticationService.class);
		assertThat(authenticationService, is(notNullValue()));
		assertThat(authenticationService, is(authenticationServiceMock));
	}

	@Test
	public void testCreate() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new NoteModulesMock());
		final NoteService noteService = injector.getInstance(NoteService.class);
		final AuthenticationServiceMock authenticationService = injector.getInstance(AuthenticationServiceMock.class);
		final SessionIdentifier sessionIdentifier = authenticationService.mockSessionIdentifier();
		authenticationService.mockLoginUser(sessionIdentifier);
		assertThat(authenticationService.isLoggedIn(sessionIdentifier), is(true));
		final NoteDto noteDto = new NoteDto();
		final String title = "test";
		noteDto.setTitle(title);
		final String content = "";
		noteDto.setContent(content);
		final NoteIdentifier noteIdentifier = noteService.createNote(sessionIdentifier, noteDto);
		assertThat(noteIdentifier, is(not(nullValue())));
		final Note note = noteService.getNote(sessionIdentifier, noteIdentifier);
		assertThat(note, is(not(nullValue())));
		assertThat(note.getTitle(), is(title));
		assertThat(note.getContent(), is(content));
	}

}
