package de.benjaminborbe.analytics.dao;

import de.benjaminborbe.analytics.api.AnalyticsReportIdentifier;
import de.benjaminborbe.analytics.api.ReportValue;
import de.benjaminborbe.analytics.api.ReportValueIterator;
import de.benjaminborbe.storage.api.StorageException;

public interface AnalyticsReportValueDao {

	ReportValueIterator valueIterator(AnalyticsReportIdentifier analyticsReportIdentifier) throws StorageException;

	void addData(AnalyticsReportIdentifier analyticsReportIdentifier, final ReportValue reportValue) throws StorageException;
}
