package de.benjaminborbe.lunch.dao;

import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.tools.Dao;
import de.benjaminborbe.storage.tools.IdentifierIterator;

public interface LunchUserSettingsDao extends Dao<LunchUserSettingsBean, LunchUserSettingsIdentifier> {

	LunchUserSettingsBean findOrCreate(UserIdentifier userIdentifier) throws StorageException;

	LunchUserSettingsBean findOrCreate(LunchUserSettingsIdentifier id) throws StorageException;

	IdentifierIterator<UserIdentifier> getActivUserIdentifierIterator() throws StorageException;

}
