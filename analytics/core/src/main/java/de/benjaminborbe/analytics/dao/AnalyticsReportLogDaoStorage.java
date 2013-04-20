package de.benjaminborbe.analytics.dao;

import javax.inject.Inject;
import de.benjaminborbe.analytics.api.AnalyticsReportIdentifier;
import de.benjaminborbe.analytics.api.AnalyticsReportValue;
import de.benjaminborbe.analytics.api.AnalyticsServiceException;
import de.benjaminborbe.analytics.util.MapperCalendarFixLength;
import de.benjaminborbe.storage.api.StorageColumn;
import de.benjaminborbe.storage.api.StorageColumnIterator;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.api.StorageIterator;
import de.benjaminborbe.storage.api.StorageService;
import de.benjaminborbe.storage.api.StorageValue;
import de.benjaminborbe.tools.util.Counter;
import de.benjaminborbe.tools.util.ParseException;
import de.benjaminborbe.tools.util.ParseUtil;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class AnalyticsReportLogDaoStorage implements AnalyticsReportLogDao {

	private static final String SEPERATOR = "-";

	private final class ReportValueIteratorImpl implements AnalyticsReportLogIterator {

		private final StorageColumnIterator i;

		private ReportValueIteratorImpl(final StorageColumnIterator i) {
			this.i = i;
		}

		@Override
		public boolean hasNext() throws AnalyticsServiceException {
			try {
				return i.hasNext();
			} catch (final StorageException e) {
				throw new AnalyticsServiceException(e);
			}
		}

		@Override
		public AnalyticsReportLogValue next() throws AnalyticsServiceException {
			try {
				final StorageColumn column = i.next();
				final String columnName = column.getColumnName().getString();
				final String[] parts = columnName.split(SEPERATOR);
				return new AnalyticsReportLogValueDto(columnName, mapperCalendar.fromString(parts[0]), parseUtil.parseDouble(column.getColumnValue().getString()));
			} catch (final StorageException | ParseException | UnsupportedEncodingException e) {
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
	public AnalyticsReportLogIterator valueIterator(final AnalyticsReportIdentifier analyticsReportIdentifier) throws StorageException {
		return new ReportValueIteratorImpl(storageService.columnIteratorReversed(COLUMN_FAMILY, new StorageValue(buildKey(analyticsReportIdentifier), storageService.getEncoding())));
	}

	@Override
	public void addReportValue(final AnalyticsReportIdentifier analyticsReportIdentifier, final AnalyticsReportValue reportValue) throws StorageException {
		final String encoding = storageService.getEncoding();
		final StringBuilder columnName = new StringBuilder();
		columnName.append(mapperCalendar.toString(reportValue.getDate()));
		columnName.append(SEPERATOR);
		columnName.append(String.valueOf(counter.incrementAndGet()));
		storageService.set(COLUMN_FAMILY, new StorageValue(buildKey(analyticsReportIdentifier), encoding), new StorageValue(columnName.toString(), encoding),
			new StorageValue(String.valueOf(reportValue.getValue()), encoding));
	}

	@Override
	public void delete(final AnalyticsReportIdentifier analyticsReportIdentifier) throws StorageException {
		final String encoding = storageService.getEncoding();
		storageService.delete(COLUMN_FAMILY, new StorageValue(buildKey(analyticsReportIdentifier), encoding));
	}

	@Override
	public void delete(final AnalyticsReportIdentifier analyticsReportIdentifier, final Collection<String> columnNames) throws StorageException {
		final String encoding = storageService.getEncoding();
		final List<StorageValue> columns = new ArrayList<>();
		for (final String columnName : columnNames) {
			columns.add(new StorageValue(columnName, encoding));
		}
		storageService.delete(COLUMN_FAMILY, new StorageValue(buildKey(analyticsReportIdentifier), encoding), columns);
	}

	private String buildKey(final AnalyticsReportIdentifier analyticsReportIdentifier) {
		final String id = analyticsReportIdentifier.getId();
		final String[] parts = id.split(String.valueOf(AnalyticsReportDao.SEPERATOR));
		return parts[0];
	}

	@Override
	public StorageIterator reportNameIterator() throws StorageException {
		return storageService.keyIterator(COLUMN_FAMILY);
	}
}
