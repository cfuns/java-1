package de.benjaminborbe.wiki.dao;

import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.tools.Dao;
import de.benjaminborbe.wiki.api.WikiSpaceIdentifier;

public interface WikiSpaceDao extends Dao<WikiSpaceBean, WikiSpaceIdentifier> {

	boolean existsSpaceWithName(String spaceName) throws StorageException;

}
