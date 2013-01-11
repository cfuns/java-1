package de.benjaminborbe.analytics.dao;

import java.io.UnsupportedEncodingException;

import com.google.inject.Inject;

import de.benjaminborbe.analytics.api.AnalyticsReportIdentifier;
import de.benjaminborbe.analytics.api.AnalyticsServiceException;
import de.benjaminborbe.analytics.api.ReportValue;
import de.benjaminborbe.analytics.api.ReportValueDto;
import de.benjaminborbe.analytics.api.ReportValueIterator;
import de.benjaminborbe.storage.api.StorageColumn;
import de.benjaminborbe.storage.api.StorageColumnIterator;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.api.StorageService;
import de.benjaminborbe.storage.api.StorageValue;
import de.benjaminborbe.tools.mapper.MapperCalendar;
import de.benjaminborbe.tools.util.ParseException;
import de.benjaminborbe.tools.util.ParseUtil;

public class AnalyticsReportValueDaoStorage implements AnalyticsReportValueDao {

	private final class ReportValueIteratorImpl implements ReportValueIterator {

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
		public ReportValue next() throws AnalyticsServiceException {
			try {
				final StorageColumn column = i.next();
				return new ReportValueDto(mapperCalendar.fromString(column.getColumnName().getString()), parseUtil.parseDouble(column.getColumnValue().getString()));
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

	private final MapperCalendar mapperCalendar;

	@Inject
	public AnalyticsReportValueDaoStorage(final StorageService storageService, final ParseUtil parseUtil, final MapperCalendar mapperCalendar) {
		this.storageService = storageService;
		this.parseUtil = parseUtil;
		this.mapperCalendar = mapperCalendar;
	}

	@Override
	public ReportValueIterator valueIterator(final AnalyticsReportIdentifier analyticsReportIdentifier) throws StorageException {
		return new ReportValueIteratorImpl(storageService.columnIteratorReversed(COLUMN_FAMILY, new StorageValue(analyticsReportIdentifier.getId(), storageService.getEncoding())));
	}

	@Override
	public void addData(final AnalyticsReportIdentifier analyticsReportIdentifier, final ReportValue reportValue) throws StorageException {
		final String encoding = storageService.getEncoding();
		storageService.set(COLUMN_FAMILY, new StorageValue(analyticsReportIdentifier.getId(), encoding), new StorageValue(mapperCalendar.toString(reportValue.getDate()), encoding),
				new StorageValue(String.valueOf(reportValue.getValue()), encoding));
	}
}
