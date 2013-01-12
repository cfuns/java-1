package de.benjaminborbe.analytics.dao;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;

import com.google.inject.Inject;

import de.benjaminborbe.analytics.api.AnalyticsReportIdentifier;
import de.benjaminborbe.analytics.api.AnalyticsServiceException;
import de.benjaminborbe.analytics.api.AnalyticsReportValue;
import de.benjaminborbe.analytics.api.AnalyticsReportValueDto;
import de.benjaminborbe.analytics.api.AnalyticsReportValueIterator;
import de.benjaminborbe.analytics.util.MapperCalendarFixLength;
import de.benjaminborbe.storage.api.StorageColumn;
import de.benjaminborbe.storage.api.StorageColumnIterator;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.api.StorageService;
import de.benjaminborbe.storage.api.StorageValue;
import de.benjaminborbe.tools.util.ParseException;
import de.benjaminborbe.tools.util.ParseUtil;

public class AnalyticsReportValueDaoStorage implements AnalyticsReportValueDao {

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
				return new AnalyticsReportValueDto(mapperCalendar.fromString(column.getColumnName().getString()), parseUtil.parseDouble(column.getColumnValue().getString()));
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

	@Inject
	public AnalyticsReportValueDaoStorage(final StorageService storageService, final ParseUtil parseUtil, final MapperCalendarFixLength mapperCalendar) {
		this.storageService = storageService;
		this.parseUtil = parseUtil;
		this.mapperCalendar = mapperCalendar;
	}

	@Override
	public AnalyticsReportValueIterator valueIterator(final AnalyticsReportIdentifier analyticsReportIdentifier) throws StorageException {
		return new ReportValueIteratorImpl(storageService.columnIteratorReversed(COLUMN_FAMILY, new StorageValue(analyticsReportIdentifier.getId(), storageService.getEncoding())));
	}

	@Override
	public void setReportValue(final AnalyticsReportIdentifier analyticsReportIdentifier, final AnalyticsReportValue reportValue) throws StorageException {
		set(analyticsReportIdentifier, reportValue.getDate(), reportValue.getValue());
	}

	@Override
	public void addReportValue(final AnalyticsReportIdentifier analyticsReportIdentifier, final AnalyticsReportValue reportValue) throws StorageException,
			UnsupportedEncodingException, ParseException {
		final String encoding = storageService.getEncoding();

		final StorageValue oldValue = storageService.get(COLUMN_FAMILY, new StorageValue(analyticsReportIdentifier.getId(), encoding),
				new StorageValue(mapperCalendar.toString(reportValue.getDate()), encoding));

		final Double value;
		if (oldValue != null && !oldValue.isEmpty()) {
			value = reportValue.getValue() + parseUtil.parseDouble(oldValue.getString());
		}
		else {
			value = reportValue.getValue();
		}
		set(analyticsReportIdentifier, reportValue.getDate(), value);
	}

	private void set(final AnalyticsReportIdentifier analyticsReportIdentifier, final Calendar date, final Double value) throws StorageException {
		final String encoding = storageService.getEncoding();
		storageService.set(COLUMN_FAMILY, new StorageValue(analyticsReportIdentifier.getId(), encoding), new StorageValue(mapperCalendar.toString(date), encoding), new StorageValue(
				String.valueOf(value), encoding));

	}

	@Override
	public void delete(final AnalyticsReportIdentifier analyticsReportIdentifier) throws StorageException {
		final String encoding = storageService.getEncoding();
		storageService.delete(COLUMN_FAMILY, new StorageValue(analyticsReportIdentifier.getId(), encoding));
	}
}
