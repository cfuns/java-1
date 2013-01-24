package de.benjaminborbe.analytics.dao;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;

import com.google.inject.Inject;

import de.benjaminborbe.analytics.api.AnalyticsReportIdentifier;
import de.benjaminborbe.analytics.api.AnalyticsReportInterval;
import de.benjaminborbe.analytics.api.AnalyticsReportValue;
import de.benjaminborbe.analytics.api.AnalyticsReportValueDto;
import de.benjaminborbe.analytics.api.AnalyticsReportValueIterator;
import de.benjaminborbe.analytics.api.AnalyticsServiceException;
import de.benjaminborbe.analytics.util.AnalyticsIntervalUtil;
import de.benjaminborbe.analytics.util.MapperCalendarFixLength;
import de.benjaminborbe.storage.api.StorageColumn;
import de.benjaminborbe.storage.api.StorageColumnIterator;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.api.StorageService;
import de.benjaminborbe.storage.api.StorageValue;
import de.benjaminborbe.tools.util.ParseException;
import de.benjaminborbe.tools.util.ParseUtil;

public class AnalyticsReportValueDaoStorage implements AnalyticsReportValueDao {

	private static final String VALUE_SEPERATOR = "_";

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
				return buildAnalyticsReportValue(column.getColumnName().getString(), column.getColumnValue().getString());
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

	private static final String COLUMN_FAMILY = "analytics_report_value";

	private final StorageService storageService;

	private final ParseUtil parseUtil;

	private final MapperCalendarFixLength mapperCalendar;

	private final AnalyticsIntervalUtil analyticsIntervalUtil;

	@Inject
	public AnalyticsReportValueDaoStorage(
			final StorageService storageService,
			final ParseUtil parseUtil,
			final MapperCalendarFixLength mapperCalendar,
			final AnalyticsIntervalUtil analyticsIntervalUtil) {
		this.storageService = storageService;
		this.parseUtil = parseUtil;
		this.mapperCalendar = mapperCalendar;
		this.analyticsIntervalUtil = analyticsIntervalUtil;
	}

	@Override
	public AnalyticsReportValueIterator valueIterator(final AnalyticsReportIdentifier analyticsReportIdentifier, final AnalyticsReportInterval analyticsReportInterval)
			throws StorageException {
		return valueIterator(createAnalyticsReportValueIdentifier(analyticsReportIdentifier, analyticsReportInterval));
	}

	@Override
	public AnalyticsReportValueIterator valueIterator(final AnalyticsReportValueIdentifier analyticsReportValueIdentifier) throws StorageException {
		return new ReportValueIteratorImpl(storageService.columnIteratorReversed(COLUMN_FAMILY, new StorageValue(analyticsReportValueIdentifier.getId(), storageService.getEncoding())));
	}

	@Override
	public void setReportValue(final AnalyticsReportIdentifier analyticsReportIdentifier, final AnalyticsReportInterval analyticsReportInterval,
			final AnalyticsReportValue reportValue) throws StorageException, UnsupportedEncodingException, ParseException {
		setReportValue(createAnalyticsReportValueIdentifier(analyticsReportIdentifier, analyticsReportInterval), reportValue);
	}

	@Override
	public void setReportValue(final AnalyticsReportValueIdentifier analyticsReportValueIdentifier, final AnalyticsReportValue reportValue) throws StorageException,
			UnsupportedEncodingException, ParseException {
		final String encoding = storageService.getEncoding();
		storageService.set(

		COLUMN_FAMILY,

		new StorageValue(analyticsReportValueIdentifier.getId(), encoding),

		new StorageValue(mapperCalendar.toString(analyticsIntervalUtil.buildIntervalCalendar(reportValue.getDate(), analyticsReportValueIdentifier.getReportInterval())), encoding),

		new StorageValue(String.valueOf(reportValue.getValue()) + VALUE_SEPERATOR + String.valueOf(reportValue.getCounter()), encoding)

		);
	}

	@Override
	public void delete(final AnalyticsReportIdentifier analyticsReportIdentifier) throws StorageException {
		for (final AnalyticsReportInterval analyticsReportInterval : AnalyticsReportInterval.values()) {
			delete(analyticsReportIdentifier, analyticsReportInterval);
		}
	}

	@Override
	public void delete(final AnalyticsReportIdentifier analyticsReportIdentifier, final AnalyticsReportInterval analyticsReportInterval) throws StorageException {
		delete(createAnalyticsReportValueIdentifier(analyticsReportIdentifier, analyticsReportInterval));
	}

	@Override
	public void delete(final AnalyticsReportValueIdentifier analyticsReportValueIdentifier) throws StorageException {
		final String encoding = storageService.getEncoding();
		storageService.delete(COLUMN_FAMILY, new StorageValue(analyticsReportValueIdentifier.getId(), encoding));
	}

	private AnalyticsReportValueIdentifier createAnalyticsReportValueIdentifier(final AnalyticsReportIdentifier analyticsReportIdentifier,
			final AnalyticsReportInterval analyticsReportInterval) {
		return new AnalyticsReportValueIdentifier(analyticsReportIdentifier, analyticsReportInterval);
	}

	@Override
	public AnalyticsReportValue getReportValue(final AnalyticsReportIdentifier analyticsReportIdentifier, final AnalyticsReportInterval analyticsReportInterval,
			final Calendar calendar) throws StorageException, UnsupportedEncodingException, ParseException {
		return getReportValue(createAnalyticsReportValueIdentifier(analyticsReportIdentifier, analyticsReportInterval), calendar);
	}

	@Override
	public AnalyticsReportValue getReportValue(final AnalyticsReportValueIdentifier analyticsReportValueIdentifier, final Calendar calendar) throws StorageException,
			UnsupportedEncodingException, ParseException {
		final String columnName = mapperCalendar.toString(analyticsIntervalUtil.buildIntervalCalendar(calendar, analyticsReportValueIdentifier.getReportInterval()));
		final String encoding = storageService.getEncoding();
		final StorageValue value = storageService.get(COLUMN_FAMILY, new StorageValue(analyticsReportValueIdentifier.getId(), encoding), new StorageValue(columnName, encoding));
		if (value != null && !value.isEmpty()) {
			final String columnValue = value.getString();
			return buildAnalyticsReportValue(columnName, columnValue);
		}
		else {
			return null;
		}
	}

	private AnalyticsReportValue buildAnalyticsReportValue(final String columnName, final String columnValue) throws ParseException {
		final Calendar calendar = mapperCalendar.fromString(columnName);

		final String[] parts = columnValue.split(VALUE_SEPERATOR, 2);
		if (parts.length == 2) {
			return new AnalyticsReportValueDto(calendar, parseUtil.parseDouble(parts[0]), parseUtil.parseLong(parts[1]));
		}
		else {
			return new AnalyticsReportValueDto(calendar, parseUtil.parseDouble(parts[0]), 1l);
		}

	}
}
