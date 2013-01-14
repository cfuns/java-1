package de.benjaminborbe.dashboard.service;

import java.io.UnsupportedEncodingException;
import java.util.Collection;

import com.google.inject.Inject;

import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.AuthenticationServiceException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.dashboard.api.DashboardIdentifier;
import de.benjaminborbe.dashboard.api.DashboardService;
import de.benjaminborbe.dashboard.api.DashboardServiceException;
import de.benjaminborbe.dashboard.dao.DashboardDao;
import de.benjaminborbe.storage.api.StorageException;

public class DashboardServiceImpl implements DashboardService {

	private final AuthenticationService authenticationService;

	private final DashboardDao dashboardDao;

	@Inject
	public DashboardServiceImpl(final AuthenticationService authenticationService, final DashboardDao dashboardDao) {
		this.authenticationService = authenticationService;
		this.dashboardDao = dashboardDao;
	}

	@Override
	public Collection<DashboardIdentifier> getSelectedDashboards(final SessionIdentifier sessionIdentifier) throws DashboardServiceException, LoginRequiredException {
		try {
			authenticationService.expectLoggedIn(sessionIdentifier);
			final UserIdentifier userIdentifier = authenticationService.getCurrentUser(sessionIdentifier);
			return dashboardDao.getSelectedDashboards(userIdentifier);
		}
		catch (final AuthenticationServiceException e) {
			throw new DashboardServiceException(e);
		}
		catch (final UnsupportedEncodingException e) {
			throw new DashboardServiceException(e);
		}
		catch (final StorageException e) {
			throw new DashboardServiceException(e);
		}
	}

	@Override
	public void selectDashboard(final SessionIdentifier sessionIdentifier, final DashboardIdentifier dashboardIdentifier) throws DashboardServiceException, LoginRequiredException {
		try {
			authenticationService.expectLoggedIn(sessionIdentifier);
			final UserIdentifier userIdentifier = authenticationService.getCurrentUser(sessionIdentifier);
			dashboardDao.selectDashboard(userIdentifier, dashboardIdentifier);
		}
		catch (final AuthenticationServiceException e) {
			throw new DashboardServiceException(e);
		}
		catch (final StorageException e) {
			throw new DashboardServiceException(e);
		}
	}

	@Override
	public void deselectDashboard(final SessionIdentifier sessionIdentifier, final DashboardIdentifier dashboardIdentifier) throws DashboardServiceException, LoginRequiredException {
		try {
			authenticationService.expectLoggedIn(sessionIdentifier);
			final UserIdentifier userIdentifier = authenticationService.getCurrentUser(sessionIdentifier);
			dashboardDao.deselectDashboard(userIdentifier, dashboardIdentifier);
		}
		catch (final AuthenticationServiceException e) {
			throw new DashboardServiceException(e);
		}
		catch (final StorageException e) {
			throw new DashboardServiceException(e);
		}
	}

}
