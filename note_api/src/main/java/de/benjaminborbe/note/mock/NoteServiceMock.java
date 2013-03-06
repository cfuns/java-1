package de.benjaminborbe.note.mock;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.note.api.NoteService;

@Singleton
public class NoteServiceMock implements NoteService {

	@Inject
	public NoteServiceMock() {
	}

	@Override
	public void execute() {
	}
}
