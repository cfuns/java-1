package de.benjaminborbe.analytics.dao;

import java.io.UnsupportedEncodingException;

import com.google.inject.Inject;

import de.benjaminborbe.analytics.api.AnalyticsReportIdentifier;
import de.benjaminborbe.analytics.api.AnalyticsReportValue;
import de.benjaminborbe.analytics.api.AnalyticsReportValueDto;
import de.benjaminborbe.analytics.api.AnalyticsReportValueIterator;
import de.benjaminborbe.analytics.api.AnalyticsServiceException;
import de.benjaminborbe.analytics.util.MapperCalendarFixLength;
import de.benjaminborbe.storage.api.StorageColumn;
import de.benjaminborbe.storage.api.StorageColumnIterator;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.api.StorageService;
import de.benjaminborbe.storage.api.StorageValue;
import de.benjaminborbe.tools.util.Counter;
import de.benjaminborbe.tools.util.ParseException;
import de.benjaminborbe.tools.util.ParseUtil;

public class AnalyticsReportLogDaoStorage implements AnalyticsReportLogDao {

	private static final String SEPERATOR = "-";

	private final class ReportValueIteratorImpl implements AnalyticsReportValueIterator {

		private final StorageColumnIterator i;

		private ReportValueIteratorImpl(final StorageColumnIterator i) {
			this.i = i;
		}

		@Override
		public boolean hasNext() throws AnalyticsServiceException {
			try {
				return i.hasNext();
			}
			catch (final StorageException e) {
				throw new AnalyticsServiceException(e);
			}
		}

		@Override
		public AnalyticsReportValue next() throws AnalyticsServiceException {
			try {
				final StorageColumn column = i.next();
				final String columnName = column.getColumnName().getString();
				final String[] parts = columnName.split(SEPERATOR);
				return new AnalyticsReportValueDto(mapperCalendar.fromString(parts[0]), parseUtil.parseDouble(column.getColumnValue().getString()));
			}
			catch (final StorageException e) {
				throw new AnalyticsServiceException(e);
			}
			catch (final UnsupportedEncodingException e) {
				throw new AnalyticsServiceException(e);
			}
			catch (final ParseException e) {
				throw new AnalyticsServiceException(e);
			}
		}
	}

	private static final String COLUMN_FAMILY = "analytics_report_log";

	private final StorageService storageService;

	private final MapperCalendarFixLength mapperCalendar;

	private final ParseUtil parseUtil;

	private final Counter counter;

	@Inject
	public AnalyticsReportLogDaoStorage(final StorageService storageService, final MapperCalendarFixLength mapperCalendar, final ParseUtil parseUtil, final Counter counter) {
		this.storageService = storageService;
		this.mapperCalendar = mapperCalendar;
		this.parseUtil = parseUtil;
		this.counter = counter;
	}

	@Override
	public AnalyticsReportValueIterator valueIterator(final AnalyticsReportIdentifier analyticsReportIdentifier) throws StorageException {
		return new ReportValueIteratorImpl(storageService.columnIteratorReversed(COLUMN_FAMILY, new StorageValue(analyticsReportIdentifier.getId(), storageService.getEncoding())));
	}

	@Override
	public void addReportValue(final AnalyticsReportIdentifier analyticsReportIdentifier, final AnalyticsReportValue reportValue) throws StorageException {
		final String encoding = storageService.getEncoding();
		final StringBuilder columnName = new StringBuilder();
		columnName.append(mapperCalendar.toString(reportValue.getDate()));
		columnName.append(SEPERATOR);
		columnName.append(String.valueOf(counter.incrementAndGet()));
		storageService.set(COLUMN_FAMILY, new StorageValue(analyticsReportIdentifier.getId(), encoding), new StorageValue(columnName.toString(), encoding),
				new StorageValue(String.valueOf(reportValue.getValue()), encoding));
	}

	@Override
	public void delete(final AnalyticsReportIdentifier analyticsReportIdentifier) throws StorageException {
		final String encoding = storageService.getEncoding();
		storageService.delete(COLUMN_FAMILY, new StorageValue(analyticsReportIdentifier.getId(), encoding));
	}

}
