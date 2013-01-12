package de.benjaminborbe.analytics.dao;

import de.benjaminborbe.analytics.api.AnalyticsReportIdentifier;
import de.benjaminborbe.analytics.api.AnalyticsReportValue;
import de.benjaminborbe.analytics.api.AnalyticsReportValueIterator;
import de.benjaminborbe.storage.api.StorageException;

public interface AnalyticsReportLogDao {

	void addReportValue(AnalyticsReportIdentifier analyticsReportIdentifier, AnalyticsReportValue reportValue) throws StorageException;

	void delete(AnalyticsReportIdentifier analyticsIdentifier) throws StorageException;

	AnalyticsReportValueIterator valueIterator(AnalyticsReportIdentifier analyticsReportIdentifier) throws StorageException;

}
