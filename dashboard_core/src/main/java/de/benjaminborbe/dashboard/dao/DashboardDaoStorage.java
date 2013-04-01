package de.benjaminborbe.dashboard.dao;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.inject.Inject;

import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.dashboard.api.DashboardIdentifier;
import de.benjaminborbe.storage.api.StorageColumn;
import de.benjaminborbe.storage.api.StorageColumnIterator;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.api.StorageService;
import de.benjaminborbe.storage.api.StorageValue;

public class DashboardDaoStorage implements DashboardDao {

	private final StorageService storageService;

	private final String COLUMN_FAMILY = "dashboard";

	@Inject
	public DashboardDaoStorage(final StorageService storageService) {
		this.storageService = storageService;
	}

	@Override
	public Collection<DashboardIdentifier> getSelectedDashboards(final UserIdentifier userIdentifier) throws StorageException, UnsupportedEncodingException {
		final List<DashboardIdentifier> result = new ArrayList<DashboardIdentifier>();
		final StorageColumnIterator columnIterator = storageService.columnIterator(COLUMN_FAMILY, new StorageValue(String.valueOf(userIdentifier), storageService.getEncoding()));
		while (columnIterator.hasNext()) {
			final StorageColumn column = columnIterator.next();
			if ("true".equals(column.getColumnValue().getString())) {
				result.add(new DashboardIdentifier(column.getColumnName().getString()));
			}
		}
		return result;
	}

	@Override
	public void selectDashboard(final UserIdentifier userIdentifier, final DashboardIdentifier dashboardIdentifier) throws StorageException {
		storageService.set(COLUMN_FAMILY, new StorageValue(String.valueOf(userIdentifier), storageService.getEncoding()), new StorageValue(String.valueOf(dashboardIdentifier),
				storageService.getEncoding()), new StorageValue("true", storageService.getEncoding()));
	}

	@Override
	public void deselectDashboard(final UserIdentifier userIdentifier, final DashboardIdentifier dashboardIdentifier) throws StorageException {
		storageService.set(COLUMN_FAMILY, new StorageValue(String.valueOf(userIdentifier), storageService.getEncoding()), new StorageValue(String.valueOf(dashboardIdentifier),
				storageService.getEncoding()), new StorageValue("false", storageService.getEncoding()));
	}

}
