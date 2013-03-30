package de.benjaminborbe.virt.core.dao;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import de.benjaminborbe.storage.api.StorageService;
import de.benjaminborbe.storage.tools.DaoStorage;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.virt.api.VirtNetworkIdentifier;
import org.slf4j.Logger;

@Singleton
public class VirtNetworkDaoStorage extends DaoStorage<VirtNetworkBean, VirtNetworkIdentifier> implements VirtNetworkDao {

	@Inject
	public VirtNetworkDaoStorage(
		final Logger logger,
		final StorageService storageService,
		final Provider<VirtNetworkBean> beanProvider,
		final VirtNetworkBeanMapper mapper,
		final VirtNetworkIdentifierBuilder identifierBuilder,
		final CalendarUtil calendarUtil) {
		super(logger, storageService, beanProvider, mapper, identifierBuilder, calendarUtil);
	}

	private static final String COLUMN_FAMILY = "checklist_list";

	@Override
	protected String getColumnFamily() {
		return COLUMN_FAMILY;
	}

}
