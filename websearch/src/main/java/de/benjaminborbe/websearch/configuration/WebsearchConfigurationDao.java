package de.benjaminborbe.websearch.configuration;

import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.tools.Dao;
import de.benjaminborbe.storage.tools.EntityIterator;
import de.benjaminborbe.websearch.api.WebsearchConfigurationIdentifier;

public interface WebsearchConfigurationDao extends Dao<WebsearchConfigurationBean, WebsearchConfigurationIdentifier> {

	EntityIterator<WebsearchConfigurationBean> getActivatedEntityIterator() throws StorageException;

}
