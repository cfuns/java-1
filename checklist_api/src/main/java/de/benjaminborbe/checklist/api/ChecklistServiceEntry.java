package de.benjaminborbe.checklist.api;

import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.PermissionDeniedException;

public interface ChecklistServiceEntry {

	void delete(SessionIdentifier sessionIdentifier, ChecklistEntryIdentifier id) throws ChecklistServiceException, PermissionDeniedException, LoginRequiredException;

	ChecklistEntry read(SessionIdentifier sessionIdentifier, ChecklistEntryIdentifier id) throws ChecklistServiceException, PermissionDeniedException, LoginRequiredException;

	ChecklistEntryIdentifier create(SessionIdentifier sessionIdentifier, ChecklistEntry object) throws ChecklistServiceException, PermissionDeniedException, ValidationException,
			LoginRequiredException;

	void update(SessionIdentifier sessionIdentifier, ChecklistEntry object) throws ChecklistServiceException, PermissionDeniedException, ValidationException, LoginRequiredException;
}
