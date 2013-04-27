package de.benjaminborbe.note.api;

import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.PermissionDeniedException;

import java.util.Collection;

public interface NoteService {

	String PERMISSION = "note";

	NoteIdentifier createNote(
		SessionIdentifier sessionIdentifier,
		NoteDto noteDto
	) throws PermissionDeniedException, LoginRequiredException, NoteServiceException,
		ValidationException;

	void updateNote(
		SessionIdentifier sessionIdentifier,
		NoteDto noteDto
	) throws PermissionDeniedException, LoginRequiredException, NoteServiceException, ValidationException;

	void deleteNote(
		SessionIdentifier sessionIdentifier,
		NoteIdentifier noteIdentifier
	) throws PermissionDeniedException, LoginRequiredException, NoteServiceException;

	Collection<Note> getNodes(SessionIdentifier sessionIdentifier) throws PermissionDeniedException, LoginRequiredException, NoteServiceException;

	NoteIdentifier createNoteIdentifier(String id) throws NoteServiceException;

	Note getNote(
		SessionIdentifier sessionIdentifier,
		NoteIdentifier noteIdentifier
	) throws PermissionDeniedException, LoginRequiredException, NoteServiceException;
}
