package de.benjaminborbe.checklist.dao;

import com.google.inject.Provider;
import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.checklist.api.ChecklistEntryIdentifier;
import de.benjaminborbe.checklist.api.ChecklistListIdentifier;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.api.StorageService;
import de.benjaminborbe.storage.tools.DaoStorage;
import de.benjaminborbe.storage.tools.EntityIterator;
import de.benjaminborbe.storage.tools.StorageValueMap;
import de.benjaminborbe.tools.date.CalendarUtil;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ChecklistEntryDaoStorage extends DaoStorage<ChecklistEntryBean, ChecklistEntryIdentifier> implements ChecklistEntryDao {

	@Inject
	public ChecklistEntryDaoStorage(
		final Logger logger,
		final StorageService storageService,
		final Provider<ChecklistEntryBean> beanProvider,
		final ChecklistEntryBeanMapper mapper,
		final ChecklistEntryIdentifierBuilder identifierBuilder,
		final CalendarUtil calendarUtil
	) {
		super(logger, storageService, beanProvider, mapper, identifierBuilder, calendarUtil);
	}

	private static final String COLUMN_FAMILY = "checklist_entry";

	@Override
	protected String getColumnFamily() {
		return COLUMN_FAMILY;
	}

	@Override
	public EntityIterator<ChecklistEntryBean> getEntityIteratorForListAndUser(
		final ChecklistListIdentifier checklistListIdentifier,
		final UserIdentifier userIdentifier
	)
		throws StorageException {
		return getEntityIterator(new StorageValueMap(getEncoding()).add(ChecklistEntryBeanMapper.OWNER, String.valueOf(userIdentifier)).add(ChecklistEntryBeanMapper.LIST_ID,
			String.valueOf(checklistListIdentifier)));
	}

}
