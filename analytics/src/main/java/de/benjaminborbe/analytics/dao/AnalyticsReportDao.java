package de.benjaminborbe.analytics.dao;

import de.benjaminborbe.analytics.api.AnalyticsReportIdentifier;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.tools.Dao;
import de.benjaminborbe.storage.tools.IdentifierIteratorException;

public interface AnalyticsReportDao extends Dao<AnalyticsReportBean, AnalyticsReportIdentifier> {

	char SEPERATOR = '_';

	boolean existsReportWithName(String name) throws StorageException, IdentifierIteratorException;

}
