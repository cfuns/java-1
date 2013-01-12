package de.benjaminborbe.analytics.dao;

import java.io.UnsupportedEncodingException;

import de.benjaminborbe.analytics.api.AnalyticsReportIdentifier;
import de.benjaminborbe.analytics.api.AnalyticsReportValue;
import de.benjaminborbe.analytics.api.AnalyticsReportValueIterator;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.tools.util.ParseException;

public interface AnalyticsReportValueDao {

	AnalyticsReportValueIterator valueIterator(AnalyticsReportIdentifier analyticsReportIdentifier) throws StorageException;

	void setReportValue(AnalyticsReportIdentifier analyticsReportIdentifier, final AnalyticsReportValue reportValue) throws StorageException;

	void addReportValue(AnalyticsReportIdentifier analyticsReportIdentifier, AnalyticsReportValue reportValue) throws StorageException, UnsupportedEncodingException, ParseException;

	void delete(AnalyticsReportIdentifier analyticsIdentifier) throws StorageException;
}
