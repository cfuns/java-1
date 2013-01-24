package de.benjaminborbe.dashboard.mock;

import java.util.Collection;

import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.dashboard.api.DashboardIdentifier;
import de.benjaminborbe.dashboard.api.DashboardService;
import de.benjaminborbe.dashboard.api.DashboardServiceException;

public class DashboardServiceMock implements DashboardService {

	@Override
	public Collection<DashboardIdentifier> getSelectedDashboards(SessionIdentifier sessionIdentifier) throws DashboardServiceException, LoginRequiredException {
		return null;
	}

	@Override
	public void selectDashboard(SessionIdentifier sessionIdentifier, DashboardIdentifier dashboardIdentifier) throws DashboardServiceException, LoginRequiredException {
	}

	@Override
	public void deselectDashboard(SessionIdentifier sessionIdentifier, DashboardIdentifier dashboardIdentifier) throws DashboardServiceException, LoginRequiredException {
	}

}
