package de.benjaminborbe.dhl.mock;

import java.net.URL;
import java.util.Collection;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.dhl.api.Dhl;
import de.benjaminborbe.dhl.api.DhlIdentifier;
import de.benjaminborbe.dhl.api.DhlService;
import de.benjaminborbe.dhl.api.DhlServiceException;

@Singleton
public class DhlServiceMock implements DhlService {

	@Inject
	public DhlServiceMock() {
	}

	@Override
	public URL buildDhlUrl(final SessionIdentifier sessionIdentifier, final DhlIdentifier dhlIdentifier) throws DhlServiceException {
		return null;
	}

	@Override
	public void mailStatus(final SessionIdentifier sessionIdentifier, final DhlIdentifier dhlIdentifier) throws DhlServiceException {
	}

	@Override
	public Collection<DhlIdentifier> getIdentifiers(final SessionIdentifier sessionIdentifier) throws DhlServiceException {
		return null;
	}

	@Override
	public void removeTracking(final SessionIdentifier sessionIdentifier, final DhlIdentifier dhlIdentifier) throws DhlServiceException {
	}

	@Override
	public DhlIdentifier createDhlIdentifier(final String id) throws DhlServiceException {
		return null;
	}

	@Override
	public DhlIdentifier addTracking(final SessionIdentifier sessionIdentifier, final String trackingNumber, final long zip) throws DhlServiceException {
		return null;
	}

	@Override
	public void triggerCheck(final SessionIdentifier sessionIdentifier) throws DhlServiceException, LoginRequiredException, PermissionDeniedException {
	}

	@Override
	public Collection<Dhl> getEntries(SessionIdentifier sessionIdentifier) throws DhlServiceException, LoginRequiredException, PermissionDeniedException {
		return null;
	}

}
