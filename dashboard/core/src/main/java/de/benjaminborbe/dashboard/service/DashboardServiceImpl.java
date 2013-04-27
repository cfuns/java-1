package de.benjaminborbe.dashboard.service;

import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.AuthenticationServiceException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.AuthorizationServiceException;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.dashboard.api.DashboardIdentifier;
import de.benjaminborbe.dashboard.api.DashboardService;
import de.benjaminborbe.dashboard.api.DashboardServiceException;
import de.benjaminborbe.dashboard.dao.DashboardDao;
import de.benjaminborbe.storage.api.StorageException;

import javax.inject.Inject;
import java.io.UnsupportedEncodingException;
import java.util.Collection;

public class DashboardServiceImpl implements DashboardService {

	private final AuthenticationService authenticationService;

	private final DashboardDao dashboardDao;

	private final AuthorizationService authorizationService;

	@Inject
	public DashboardServiceImpl(
		final AuthenticationService authenticationService,
		final DashboardDao dashboardDao,
		final AuthorizationService authorizationService
	) {
		this.authenticationService = authenticationService;
		this.dashboardDao = dashboardDao;
		this.authorizationService = authorizationService;
	}

	@Override
	public Collection<DashboardIdentifier> getSelectedDashboards(final SessionIdentifier sessionIdentifier) throws DashboardServiceException, LoginRequiredException,
		PermissionDeniedException {
		try {
			expectPermission(sessionIdentifier);
			final UserIdentifier userIdentifier = authenticationService.getCurrentUser(sessionIdentifier);
			return dashboardDao.getSelectedDashboards(userIdentifier);
		} catch (final AuthenticationServiceException | UnsupportedEncodingException | StorageException | AuthorizationServiceException e) {
			throw new DashboardServiceException(e);
		}
	}

	private void expectPermission(final SessionIdentifier sessionIdentifier) throws AuthenticationServiceException, LoginRequiredException, AuthorizationServiceException,
		PermissionDeniedException {
		authorizationService.expectPermission(sessionIdentifier, authorizationService.createPermissionIdentifier(PERMISSION));
	}

	@Override
	public void selectDashboard(
		final SessionIdentifier sessionIdentifier,
		final DashboardIdentifier dashboardIdentifier
	) throws DashboardServiceException, LoginRequiredException,
		PermissionDeniedException {
		try {
			expectPermission(sessionIdentifier);
			final UserIdentifier userIdentifier = authenticationService.getCurrentUser(sessionIdentifier);
			dashboardDao.selectDashboard(userIdentifier, dashboardIdentifier);
		} catch (final AuthenticationServiceException | StorageException | AuthorizationServiceException e) {
			throw new DashboardServiceException(e);
		}
	}

	@Override
	public void deselectDashboard(
		final SessionIdentifier sessionIdentifier,
		final DashboardIdentifier dashboardIdentifier
	) throws DashboardServiceException, LoginRequiredException,
		PermissionDeniedException {
		try {
			expectPermission(sessionIdentifier);
			final UserIdentifier userIdentifier = authenticationService.getCurrentUser(sessionIdentifier);
			dashboardDao.deselectDashboard(userIdentifier, dashboardIdentifier);
		} catch (final AuthenticationServiceException | StorageException | AuthorizationServiceException e) {
			throw new DashboardServiceException(e);
		}
	}

}
