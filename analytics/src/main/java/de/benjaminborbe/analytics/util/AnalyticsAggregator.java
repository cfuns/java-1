package de.benjaminborbe.analytics.util;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;

import com.google.common.collect.Lists;
import com.google.inject.Inject;

import de.benjaminborbe.analytics.api.AnalyticsReportAggregation;
import de.benjaminborbe.analytics.api.AnalyticsReportIdentifier;
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
import de.benjaminborbe.tools.map.MapList;
import de.benjaminborbe.tools.synchronize.RunOnlyOnceATime;
import de.benjaminborbe.tools.util.ParseException;

public class AnalyticsAggregator {

	private final class AggregateRunnable implements Runnable {

		@Override
		public void run() {
			try {
				logger.debug("aggregate started");

				final MapList<String, AnalyticsReportBean> reportMap = new MapList<String, AnalyticsReportBean>();

				final EntityIterator<AnalyticsReportBean> i = analyticsReportDao.getEntityIterator();
				while (i.hasNext()) {
					final AnalyticsReportBean report = i.next();
					reportMap.add(report.getName(), report);
				}

				for (final Entry<String, List<AnalyticsReportBean>> e : reportMap.entrySet()) {
					final List<AnalyticsReportBean> reports = e.getValue();
					logger.debug("aggregate name: " + e.getKey() + " reports: " + reports.size());
					aggregateReport(reports);
				}

				logger.debug("aggregate finished");
			}
			catch (final Exception e) {
				logger.warn("aggregate failed: " + e.getClass().getName(), e);
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

	private void aggregateReport(final List<AnalyticsReportBean> reports) throws StorageException, AnalyticsServiceException, UnsupportedEncodingException, ParseException {
		final AnalyticsReportIdentifier id = reports.get(0).getId();
		final AnalyticsReportLogIterator i = analyticsReportLogDao.valueIterator(id);

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

		logger.debug("delete " + counter + " from log " + id);
		analyticsReportLogDao.delete(id, columnNames);

		for (final AnalyticsReportBean report : reports) {
			logger.trace("aggregate report: " + report.getName() + " method: " + report.getAggregation().name().toLowerCase());
			for (final AnalyticsReportInterval analyticsReportInterval : AnalyticsReportInterval.values()) {
				final Map<String, List<AnalyticsReportValue>> data = groupByInterval(values, analyticsReportInterval);

				for (final List<AnalyticsReportValue> list : data.values()) {
					final Calendar calendar = analyticsIntervalUtil.buildIntervalCalendar(list.get(0).getDate(), analyticsReportInterval);
					final AnalyticsReportValue oldValue = analyticsReportValueDao.getReportValue(report.getId(), analyticsReportInterval, calendar);
					final AnalyticsReportValue reportValue = buildAggregatedValue(report.getAggregation(), oldValue, calendar, list);
					if (reportValue != null) {
						analyticsReportValueDao.setReportValue(report.getId(), analyticsReportInterval, reportValue);
					}
				}
			}
		}
	}

	private AnalyticsReportValue buildAggregatedValue(final AnalyticsReportAggregation aggregation, final AnalyticsReportValue oldValue, final Calendar calendar,
			final List<AnalyticsReportValue> list) {
		if (AnalyticsReportAggregation.SUM.equals(aggregation)) {
			double value = 0d;
			long counter = 0;
			if (oldValue != null) {
				counter += oldValue.getCounter();
				value += oldValue.getValue();
			}
			for (final AnalyticsReportValue e : list) {
				value += e.getValue();
				counter += e.getCounter();
			}
			return new AnalyticsReportValueDto(calendar, value, counter);
		}
		if (AnalyticsReportAggregation.LATEST.equals(aggregation)) {
			AnalyticsReportValue value = oldValue;
			for (final AnalyticsReportValue e : list) {
				if (value == null || e.getDate().after(value.getDate())) {
					value = e;
				}
			}
			return value;
		}
		if (AnalyticsReportAggregation.OLDEST.equals(aggregation)) {
			if (oldValue != null) {
				return oldValue;
			}
			AnalyticsReportValue value = null;
			for (final AnalyticsReportValue e : list) {
				if (value == null || e.getDate().before(value.getDate())) {
					value = e;
				}
			}
			return value;
		}
		if (AnalyticsReportAggregation.AVG.equals(aggregation)) {
			double value = 0d;
			long counter = 0;
			if (oldValue != null) {
				counter += oldValue.getCounter();
				value += oldValue.getValue() * counter;
			}
			for (final AnalyticsReportValue e : list) {
				value += e.getValue();
				counter += e.getCounter();
			}
			return new AnalyticsReportValueDto(calendar, value / counter, counter);
		}
		if (AnalyticsReportAggregation.MIN.equals(aggregation)) {
			Double value = null;
			if (oldValue != null) {
				value = oldValue.getValue();
			}
			for (final AnalyticsReportValue e : list) {
				if (value == null || value > e.getValue()) {
					value = e.getValue();
				}
			}
			return new AnalyticsReportValueDto(calendar, value, 1l);
		}
		if (AnalyticsReportAggregation.MAX.equals(aggregation)) {
			Double value = null;
			if (oldValue != null) {
				value = oldValue.getValue();
			}
			for (final AnalyticsReportValue e : list) {
				if (value == null || value < e.getValue()) {
					value = e.getValue();
				}
			}
			return new AnalyticsReportValueDto(calendar, value, 1l);
		}
		return null;
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
