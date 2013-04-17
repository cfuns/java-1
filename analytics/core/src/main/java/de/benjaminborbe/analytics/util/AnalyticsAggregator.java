package de.benjaminborbe.analytics.util;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import de.benjaminborbe.analytics.api.AnalyticsReport;
import de.benjaminborbe.analytics.api.AnalyticsReportAggregation;
import de.benjaminborbe.analytics.api.AnalyticsReportIdentifier;
import de.benjaminborbe.analytics.api.AnalyticsReportInterval;
import de.benjaminborbe.analytics.api.AnalyticsReportValue;
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
import org.slf4j.Logger;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class AnalyticsAggregator {

	private final class AggregateRunnable implements Runnable {

		@Override
		public void run() {
			try {
				logger.debug("aggregate started");

				final MapList<String, AnalyticsReportBean> reportMap = new MapList<>();

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
			} catch (final Exception e) {
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

	private final AnalyticsAggregatorCalculatorFactory analyticsAggregatorCalculatorFactory;

	@Inject
	public AnalyticsAggregator(
		final Logger logger,
		final AnalyticsAggregatorCalculatorFactory analyticsAggregatorCalculatorFactory,
		final AnalyticsIntervalUtil analyticsIntervalUtil,
		final AnalyticsConfig analyticsConfig,
		final RunOnlyOnceATime runOnlyOnceATime,
		final AnalyticsReportDao analyticsReportDao,
		final AnalyticsReportValueDao analyticsReportValueDao,
		final AnalyticsReportLogDao analyticsReportLogDao) {
		this.logger = logger;
		this.analyticsAggregatorCalculatorFactory = analyticsAggregatorCalculatorFactory;
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
		} else {
			logger.debug("analytics aggregate - skipped");
			return false;
		}
	}

	private void aggregateReport(final List<AnalyticsReportBean> reports) throws StorageException, AnalyticsServiceException, UnsupportedEncodingException, ParseException {
		final AnalyticsReportIdentifier id = reports.get(0).getId();
		final AnalyticsReportLogIterator i = analyticsReportLogDao.valueIterator(id);

		// read chunkSize values to aggregate
		final List<AnalyticsReportValue> values = new ArrayList<>();
		final List<String> columnNames = new ArrayList<>();
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

		final List<AnalyticsReportValue> values = Lists.newArrayList(list);
		if (oldValue != null) {
			values.add(oldValue);
		}

		final AnalyticsAggregatorCalculator analyticsAggregatorCalculator = analyticsAggregatorCalculatorFactory.get(aggregation);
		return analyticsAggregatorCalculator.aggregate(calendar, values);
	}

	private Map<String, List<AnalyticsReportValue>> groupByInterval(final List<AnalyticsReportValue> values, final AnalyticsReportInterval analyticsReportInterval) {
		final Map<String, List<AnalyticsReportValue>> data = new HashMap<>();
		for (final AnalyticsReportValue value : values) {
			final String key = buildKey(analyticsReportInterval, value.getDate());
			final List<AnalyticsReportValue> list = data.get(key);
			if (list != null) {
				list.add(value);
			} else {
				data.put(key, Lists.newArrayList(value));
			}
		}
		return data;
	}

	private String buildKey(final AnalyticsReportInterval analyticsReportInterval, final Calendar date) {
		final Calendar calendar = analyticsIntervalUtil.buildIntervalCalendar(date, analyticsReportInterval);
		return String.valueOf(calendar.getTimeInMillis());
	}

	public void rebuildReport(final AnalyticsReport analyticsReport, final List<AnalyticsReportValue> values) throws StorageException, UnsupportedEncodingException, ParseException {
		logger.debug("rebuildReport - id: " + analyticsReport.getId() + " values: " + values.size());
		for (final AnalyticsReportInterval analyticsReportInterval : AnalyticsReportInterval.values()) {
			logger.debug("rebuild interval " + analyticsReportInterval);
			analyticsReportValueDao.delete(analyticsReport.getId(), analyticsReportInterval);
			logger.debug("delete old report " + analyticsReport.getId() + "-" + analyticsReportInterval);
			final Map<String, List<AnalyticsReportValue>> data = groupByInterval(values, analyticsReportInterval);
			for (final List<AnalyticsReportValue> list : data.values()) {
				final Calendar calendar = analyticsIntervalUtil.buildIntervalCalendar(list.get(0).getDate(), analyticsReportInterval);
				final AnalyticsReportValue oldValue = analyticsReportValueDao.getReportValue(analyticsReport.getId(), analyticsReportInterval, calendar);
				final AnalyticsReportValue reportValue = buildAggregatedValue(analyticsReport.getAggregation(), oldValue, calendar, list);
				if (reportValue != null) {
					logger.trace("write new data - value: " + reportValue);
					analyticsReportValueDao.setReportValue(analyticsReport.getId(), analyticsReportInterval, reportValue);
				}
			}
		}
	}
}
