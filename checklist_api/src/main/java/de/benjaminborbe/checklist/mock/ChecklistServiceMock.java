package de.benjaminborbe.checklist.mock;

import java.util.Collection;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.checklist.api.ChecklistEntry;
import de.benjaminborbe.checklist.api.ChecklistEntryIdentifier;
import de.benjaminborbe.checklist.api.ChecklistList;
import de.benjaminborbe.checklist.api.ChecklistListIdentifier;
import de.benjaminborbe.checklist.api.ChecklistService;
import de.benjaminborbe.checklist.api.ChecklistServiceException;

@Singleton
public class ChecklistServiceMock implements ChecklistService {

	@Inject
	public ChecklistServiceMock() {
	}

	@Override
	public void delete(final SessionIdentifier sessionIdentifier, final ChecklistListIdentifier id) throws ChecklistServiceException, PermissionDeniedException,
			LoginRequiredException {
	}

	@Override
	public ChecklistList read(final SessionIdentifier sessionIdentifier, final ChecklistListIdentifier id) throws ChecklistServiceException, PermissionDeniedException,
			LoginRequiredException {
		return null;
	}

	@Override
	public ChecklistListIdentifier create(final SessionIdentifier sessionIdentifier, final ChecklistList object) throws ChecklistServiceException, PermissionDeniedException,
			ValidationException, LoginRequiredException {
		return null;
	}

	@Override
	public void update(final SessionIdentifier sessionIdentifier, final ChecklistList object) throws ChecklistServiceException, PermissionDeniedException, ValidationException,
			LoginRequiredException {
	}

	@Override
	public void delete(final SessionIdentifier sessionIdentifier, final ChecklistEntryIdentifier id) throws ChecklistServiceException, PermissionDeniedException,
			LoginRequiredException {
	}

	@Override
	public ChecklistEntry read(final SessionIdentifier sessionIdentifier, final ChecklistEntryIdentifier id) throws ChecklistServiceException, PermissionDeniedException,
			LoginRequiredException {
		return null;
	}

	@Override
	public ChecklistEntryIdentifier create(final SessionIdentifier sessionIdentifier, final ChecklistEntry object) throws ChecklistServiceException, PermissionDeniedException,
			ValidationException, LoginRequiredException {
		return null;
	}

	@Override
	public void update(final SessionIdentifier sessionIdentifier, final ChecklistEntry object) throws ChecklistServiceException, PermissionDeniedException, ValidationException,
			LoginRequiredException {
	}

	@Override
	public Collection<ChecklistList> getLists(final SessionIdentifier sessionIdentifier) throws ChecklistServiceException, PermissionDeniedException, LoginRequiredException {
		return null;
	}

	@Override
	public Collection<ChecklistEntry> getEntries(final SessionIdentifier sessionIdentifier, final ChecklistListIdentifier checklistListIdentifier) throws ChecklistServiceException,
			PermissionDeniedException, LoginRequiredException {
		return null;
	}

	@Override
	public void uncomplete(final SessionIdentifier sessionIdentifier, final ChecklistEntryIdentifier identifier) throws ChecklistServiceException, PermissionDeniedException,
			LoginRequiredException {
	}

	@Override
	public void complete(final SessionIdentifier sessionIdentifier, final ChecklistEntryIdentifier identifier) throws ChecklistServiceException, PermissionDeniedException,
			LoginRequiredException {
	}

}
