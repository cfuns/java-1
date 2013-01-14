package de.benjaminborbe.dashboard.dao;

import java.io.UnsupportedEncodingException;
import java.util.Collection;

import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.dashboard.api.DashboardIdentifier;
import de.benjaminborbe.storage.api.StorageException;

public interface DashboardDao {

	Collection<DashboardIdentifier> getSelectedDashboards(final UserIdentifier userIdentifier) throws StorageException, UnsupportedEncodingException;

	void selectDashboard(final UserIdentifier userIdentifier, DashboardIdentifier dashboardIdentifier) throws StorageException;

	void deselectDashboard(final UserIdentifier userIdentifier, DashboardIdentifier dashboardIdentifier) throws StorageException;
}
