package de.benjaminborbe.checklist.dao;

import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.checklist.api.ChecklistEntryIdentifier;
import de.benjaminborbe.checklist.api.ChecklistListIdentifier;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.tools.Dao;
import de.benjaminborbe.storage.tools.EntityIterator;

public interface ChecklistEntryDao extends Dao<ChecklistEntryBean, ChecklistEntryIdentifier> {

	EntityIterator<ChecklistEntryBean> getEntityIteratorForListAndUser(
		ChecklistListIdentifier checklistListIdentifier,
		UserIdentifier currentUser
	) throws StorageException;

}
