package de.benjaminborbe.note.mock;

import java.util.Collection;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.note.api.Note;
import de.benjaminborbe.note.api.NoteDto;
import de.benjaminborbe.note.api.NoteIdentifier;
import de.benjaminborbe.note.api.NoteService;
import de.benjaminborbe.note.api.NoteServiceException;

@Singleton
public class NoteServiceMock implements NoteService {

	@Inject
	public NoteServiceMock() {
	}

	@Override
	public NoteIdentifier createNote(final SessionIdentifier sessionIdentifier, final NoteDto noteDto) throws PermissionDeniedException, LoginRequiredException, NoteServiceException,
			ValidationException {
		return null;
	}

	@Override
	public void updateNote(final SessionIdentifier sessionIdentifier, final NoteDto noteDto) throws PermissionDeniedException, LoginRequiredException, NoteServiceException, ValidationException {
	}

	@Override
	public void deleteNote(final SessionIdentifier sessionIdentifier, final NoteIdentifier noteIdentifier) throws PermissionDeniedException, LoginRequiredException, NoteServiceException {
	}

	@Override
	public Collection<Note> getNodes(final SessionIdentifier sessionIdentifier) throws PermissionDeniedException, LoginRequiredException, NoteServiceException {
		return null;
	}

	@Override
	public NoteIdentifier createNoteIdentifier(final String id) throws NoteServiceException {
		return null;
	}
}
