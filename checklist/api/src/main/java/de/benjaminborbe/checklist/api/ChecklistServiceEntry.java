package de.benjaminborbe.checklist.api;

import java.util.Collection;

import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.PermissionDeniedException;

public interface ChecklistServiceEntry {

	void uncomplete(SessionIdentifier sessionIdentifier, ChecklistEntryIdentifier identifier) throws ChecklistServiceException, PermissionDeniedException, LoginRequiredException,
			ValidationException;

	void complete(SessionIdentifier sessionIdentifier, ChecklistEntryIdentifier identifier) throws ChecklistServiceException, PermissionDeniedException, LoginRequiredException,
			ValidationException;

	Collection<ChecklistEntry> getEntries(SessionIdentifier sessionIdentifier, ChecklistListIdentifier checklistListIdentifier) throws ChecklistServiceException,
			PermissionDeniedException, LoginRequiredException;

	void delete(SessionIdentifier sessionIdentifier, ChecklistEntryIdentifier id) throws ChecklistServiceException, PermissionDeniedException, LoginRequiredException;

	ChecklistEntry read(SessionIdentifier sessionIdentifier, ChecklistEntryIdentifier id) throws ChecklistServiceException, PermissionDeniedException, LoginRequiredException;

	ChecklistEntryIdentifier create(SessionIdentifier sessionIdentifier, ChecklistEntry object) throws ChecklistServiceException, PermissionDeniedException, ValidationException,
			LoginRequiredException;

	void update(SessionIdentifier sessionIdentifier, ChecklistEntry object) throws ChecklistServiceException, PermissionDeniedException, ValidationException, LoginRequiredException;
}
