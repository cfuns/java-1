package de.benjaminborbe.analytics.dao;

import java.util.Collection;

import de.benjaminborbe.analytics.api.AnalyticsReportIdentifier;
import de.benjaminborbe.analytics.api.AnalyticsReportValue;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.api.StorageIterator;

public interface AnalyticsReportLogDao {

	void addReportValue(AnalyticsReportIdentifier analyticsReportIdentifier, AnalyticsReportValue reportValue) throws StorageException;

	void delete(AnalyticsReportIdentifier analyticsReportIdentifier) throws StorageException;

	void delete(AnalyticsReportIdentifier analyticsReportIdentifier, Collection<String> columnNames) throws StorageException;

	AnalyticsReportLogIterator valueIterator(AnalyticsReportIdentifier analyticsReportIdentifier) throws StorageException;

	StorageIterator reportNameIterator() throws StorageException;

}
