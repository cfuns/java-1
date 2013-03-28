package de.benjaminborbe.dashboard.mock;

import java.util.Collection;

import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.dashboard.api.DashboardIdentifier;
import de.benjaminborbe.dashboard.api.DashboardService;
import de.benjaminborbe.dashboard.api.DashboardServiceException;

public class DashboardServiceMock implements DashboardService {

	@Override
	public Collection<DashboardIdentifier> getSelectedDashboards(final SessionIdentifier sessionIdentifier) throws DashboardServiceException, LoginRequiredException {
		return null;
	}

	@Override
	public void selectDashboard(final SessionIdentifier sessionIdentifier, final DashboardIdentifier dashboardIdentifier) throws DashboardServiceException, LoginRequiredException {
	}

	@Override
	public void deselectDashboard(final SessionIdentifier sessionIdentifier, final DashboardIdentifier dashboardIdentifier) throws DashboardServiceException, LoginRequiredException {
	}

}
