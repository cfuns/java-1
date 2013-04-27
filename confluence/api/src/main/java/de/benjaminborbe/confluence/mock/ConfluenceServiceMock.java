package de.benjaminborbe.confluence.mock;

import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.confluence.api.ConfluenceInstance;
import de.benjaminborbe.confluence.api.ConfluenceInstanceIdentifier;
import de.benjaminborbe.confluence.api.ConfluenceService;
import de.benjaminborbe.confluence.api.ConfluenceServiceException;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.Collection;

@Singleton
public class ConfluenceServiceMock implements ConfluenceService {

	@Inject
	public ConfluenceServiceMock() {
	}

	@Override
	public void deleteConfluenceInstance(final SessionIdentifier sessionIdentifier, final ConfluenceInstanceIdentifier confluenceInstanceIdentifier)
		throws ConfluenceServiceException {
	}

	@Override
	public Collection<ConfluenceInstanceIdentifier> getConfluenceInstanceIdentifiers(final SessionIdentifier sessionIdentifier) throws ConfluenceServiceException {
		return new ArrayList<>();
	}

	@Override
	public Collection<ConfluenceInstance> getConfluenceInstances(final SessionIdentifier sessionIdentifier) throws ConfluenceServiceException {
		return new ArrayList<>();
	}

	@Override
	public ConfluenceInstanceIdentifier createConfluenceInstanceIdentifier(
		final SessionIdentifier sessionIdentifier,
		final String id
	) throws ConfluenceServiceException {
		return null;
	}

	@Override
	public ConfluenceInstance getConfluenceInstance(final SessionIdentifier sessionIdentifier, final ConfluenceInstanceIdentifier confluenceInstanceIdentifier)
		throws ConfluenceServiceException, LoginRequiredException, PermissionDeniedException {
		return null;
	}

	@Override
	public void refreshSearchIndex(final SessionIdentifier sessionIdentifier) throws LoginRequiredException, ConfluenceServiceException {
	}

	@Override
	public void expireAll(final SessionIdentifier sessionIdentifier) throws ConfluenceServiceException, LoginRequiredException, PermissionDeniedException {
	}

	@Override
	public ConfluenceInstanceIdentifier createConfluenceIntance(
		final SessionIdentifier sessionIdentifier, final String url, final String username, final String password,
		final int expire, final boolean shared, final long delay, final boolean activated, final String owner
	) throws ConfluenceServiceException, LoginRequiredException,
		PermissionDeniedException, ValidationException {
		return null;
	}

	@Override
	public void updateConfluenceIntance(
		final SessionIdentifier sessionIdentifier, final ConfluenceInstanceIdentifier confluenceInstanceIdentifier, final String url,
		final String username, final String password, final int expire, final boolean shared, final long delay, final boolean activated, final String owner
	)
		throws ConfluenceServiceException, LoginRequiredException, PermissionDeniedException, ValidationException {
	}

	@Override
	public long countPages(
		final SessionIdentifier sessionIdentifier,
		final ConfluenceInstanceIdentifier confluenceInstanceIdentifier
	) throws ConfluenceServiceException,
		LoginRequiredException, PermissionDeniedException {
		return 0;
	}

	@Override
	public void expectPermission(final SessionIdentifier sessionIdentifier) throws ConfluenceServiceException, LoginRequiredException, PermissionDeniedException {
	}

	@Override
	public boolean hasPermission(final SessionIdentifier sessionIdentifier) throws ConfluenceServiceException {
		return false;
	}

}
