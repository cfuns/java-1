package de.benjaminborbe.checklist.api;

import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.PermissionDeniedException;

public interface ChecklistServiceList {

	void delete(SessionIdentifier sessionIdentifier, ChecklistListIdentifier id) throws ChecklistServiceException, PermissionDeniedException, LoginRequiredException;

	ChecklistList read(SessionIdentifier sessionIdentifier, ChecklistListIdentifier id) throws ChecklistServiceException, PermissionDeniedException, LoginRequiredException;

	ChecklistListIdentifier create(SessionIdentifier sessionIdentifier, ChecklistList object) throws ChecklistServiceException, PermissionDeniedException, ValidationException,
			LoginRequiredException;

	void update(SessionIdentifier sessionIdentifier, ChecklistList object) throws ChecklistServiceException, PermissionDeniedException, ValidationException, LoginRequiredException;
}
