package de.benjaminborbe.confluence.dao;

import de.benjaminborbe.confluence.api.ConfluenceInstanceIdentifier;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.tools.Dao;
import de.benjaminborbe.storage.tools.EntityIterator;

public interface ConfluenceInstanceDao extends Dao<ConfluenceInstanceBean, ConfluenceInstanceIdentifier> {

	EntityIterator<ConfluenceInstanceBean> getActivatedEntityIterator() throws StorageException;

}
