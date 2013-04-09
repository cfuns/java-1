package de.benjaminborbe.checklist.dao;

import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.checklist.api.ChecklistListIdentifier;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.tools.Dao;
import de.benjaminborbe.storage.tools.EntityIterator;

public interface ChecklistListDao extends Dao<ChecklistListBean, ChecklistListIdentifier> {

	EntityIterator<ChecklistListBean> getEntityIteratorForUser(UserIdentifier currentUser) throws StorageException;

}
