package de.benjaminborbe.message.dao;

import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.tools.Dao;
import de.benjaminborbe.storage.tools.EntityIterator;
import de.benjaminborbe.storage.tools.IdentifierIterator;

public interface MessageDao extends Dao<MessageBean, MessageIdentifier> {

	EntityIterator<MessageBean> getEntityIteratorForUser(String type) throws StorageException;

	IdentifierIterator<MessageIdentifier> getIdentifierIteratorForUser(String type) throws StorageException;

	EntityIterator<MessageBean> findExpired() throws StorageException;

}
