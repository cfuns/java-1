package de.benjaminborbe.analytics.util;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;

import com.google.common.collect.Lists;
import com.google.inject.Inject;

import de.benjaminborbe.analytics.api.AnalyticsReportAggregation;
import de.benjaminborbe.analytics.api.AnalyticsReportInterval;
import de.benjaminborbe.analytics.api.AnalyticsReportValue;
import de.benjaminborbe.analytics.api.AnalyticsReportValueDto;
import de.benjaminborbe.analytics.api.AnalyticsServiceException;
import de.benjaminborbe.analytics.config.AnalyticsConfig;
import de.benjaminborbe.analytics.dao.AnalyticsReportBean;
import de.benjaminborbe.analytics.dao.AnalyticsReportDao;
import de.benjaminborbe.analytics.dao.AnalyticsReportLogDao;
import de.benjaminborbe.analytics.dao.AnalyticsReportLogIterator;
import de.benjaminborbe.analytics.dao.AnalyticsReportLogValue;
import de.benjaminborbe.analytics.dao.AnalyticsReportValueDao;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.tools.EntityIterator;
import de.benjaminborbe.tools.synchronize.RunOnlyOnceATime;
import de.benjaminborbe.tools.util.ParseException;

public class AnalyticsAggregator {

	private final class AggregateRunnable implements Runnable {

		@Override
		public void run() {
			try {
				final EntityIterator<AnalyticsReportBean> i = analyticsReportDao.getEntityIterator();
				while (i.hasNext()) {
					final AnalyticsReportBean report = i.next();
					aggregateReport(report);
				}
			}
			catch (final Exception e) {
				logger.warn(e.getClass().getName());
			}
		}

	}

	private final RunOnlyOnceATime runOnlyOnceATime;

	private final Logger logger;

	private final AnalyticsReportLogDao analyticsReportLogDao;

	private final AnalyticsReportDao analyticsReportDao;

	private final AnalyticsConfig analyticsConfig;

	private final AnalyticsIntervalUtil analyticsIntervalUtil;

	private final AnalyticsReportValueDao analyticsReportValueDao;

	@Inject
	public AnalyticsAggregator(
			final Logger logger,
			final AnalyticsIntervalUtil analyticsIntervalUtil,
			final AnalyticsConfig analyticsConfig,
			final RunOnlyOnceATime runOnlyOnceATime,
			final AnalyticsReportDao analyticsReportDao,
			final AnalyticsReportValueDao analyticsReportValueDao,
			final AnalyticsReportLogDao analyticsReportLogDao) {
		this.logger = logger;
		this.analyticsIntervalUtil = analyticsIntervalUtil;
		this.analyticsConfig = analyticsConfig;
		this.runOnlyOnceATime = runOnlyOnceATime;
		this.analyticsReportDao = analyticsReportDao;
		this.analyticsReportValueDao = analyticsReportValueDao;
		this.analyticsReportLogDao = analyticsReportLogDao;
	}

	public boolean aggregate() {
		logger.debug("analytics aggregate - started");
		if (runOnlyOnceATime.run(new AggregateRunnable())) {
			logger.debug("analytics aggregate - finished");
			return true;
		}
		else {
			logger.debug("analytics aggregate - skipped");
			return false;
		}
	}

	private void aggregateReport(final AnalyticsReportBean report) throws StorageException, AnalyticsServiceException, UnsupportedEncodingException, ParseException {
		final AnalyticsReportLogIterator i = analyticsReportLogDao.valueIterator(report.getId());

		// read chunkSize values to aggregate
		final List<AnalyticsReportValue> values = new ArrayList<AnalyticsReportValue>();
		final List<String> columnNames = new ArrayList<String>();
		final long chunkSize = analyticsConfig.getAggregationChunkSize();
		long counter = 0;
		while (i.hasNext() && counter < chunkSize) {
			counter++;
			final AnalyticsReportLogValue value = i.next();
			values.add(value);
			columnNames.add(value.getColumnName());
		}

		analyticsReportLogDao.delete(report.getId(), columnNames);

		for (final AnalyticsReportInterval analyticsReportInterval : AnalyticsReportInterval.values()) {
			final Map<String, List<AnalyticsReportValue>> data = groupByInterval(values, analyticsReportInterval);

			for (final List<AnalyticsReportValue> list : data.values()) {
				final Calendar calendar = analyticsIntervalUtil.buildIntervalCalendar(list.get(0).getDate(), analyticsReportInterval);
				final Double oldValue = analyticsReportValueDao.getReportValue(report.getId(), analyticsReportInterval, calendar);
				final double value = buildAggregatedValue(report.getAggregation(), oldValue, list);
				final AnalyticsReportValueDto reportValue = new AnalyticsReportValueDto();
				reportValue.setDate(calendar);
				reportValue.setValue(value);
				analyticsReportValueDao.setReportValue(report.getId(), analyticsReportInterval, reportValue);
			}
		}
	}

	// TODO: handle AnalyticsReportAggregation
	private double buildAggregatedValue(final AnalyticsReportAggregation aggregation, final Double oldValue, final List<AnalyticsReportValue> list) {
		double result = 0d;
		if (oldValue != null) {
			result += oldValue;
		}
		for (final AnalyticsReportValue e : list) {
			result += e.getValue();
		}
		return result;
	}

	private Map<String, List<AnalyticsReportValue>> groupByInterval(final List<AnalyticsReportValue> values, final AnalyticsReportInterval analyticsReportInterval) {
		final Map<String, List<AnalyticsReportValue>> data = new HashMap<String, List<AnalyticsReportValue>>();
		for (final AnalyticsReportValue value : values) {
			final String key = buildKey(analyticsReportInterval, value.getDate());
			final List<AnalyticsReportValue> list = data.get(key);
			if (list != null) {
				list.add(value);
			}
			else {
				data.put(key, Lists.newArrayList(value));
			}
		}
		return data;
	}

	private String buildKey(final AnalyticsReportInterval analyticsReportInterval, final Calendar date) {
		final Calendar calendar = analyticsIntervalUtil.buildIntervalCalendar(date, analyticsReportInterval);
		return String.valueOf(calendar.getTimeInMillis());
	}
}
