package de.benjaminborbe.analytics.util;

import org.slf4j.Logger;

import com.google.inject.Inject;

import de.benjaminborbe.analytics.api.AnalyticsReportIdentifier;
import de.benjaminborbe.analytics.api.AnalyticsReportValue;
import de.benjaminborbe.analytics.api.AnalyticsReportValueDto;
import de.benjaminborbe.analytics.api.AnalyticsServiceException;
import de.benjaminborbe.analytics.dao.AnalyticsReportLogDao;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.queue.Queue;
import de.benjaminborbe.tools.queue.QueueBuilder;
import de.benjaminborbe.tools.queue.QueueConsumer;

public class AddValueAction {

	private final class AddMessage {

		private final AnalyticsReportIdentifier reportIdentifier;

		private final AnalyticsReportValue reportValue;

		public AddMessage(final AnalyticsReportIdentifier reportIdentifier, final AnalyticsReportValue reportValue) {
			this.reportIdentifier = reportIdentifier;
			this.reportValue = reportValue;
		}

		public AnalyticsReportValue getReportValue() {
			return reportValue;
		}

		public AnalyticsReportIdentifier getReportIdentifier() {
			return reportIdentifier;
		}
	}

	private final class AddMessageConsumer implements QueueConsumer<AddMessage> {

		@Override
		public void consume(final AddMessage message) {
			try {
				analyticsReportLogDao.addReportValue(message.getReportIdentifier(), message.getReportValue());
			}
			catch (final StorageException e) {
				logger.warn(e.getClass().getName());
			}
		}
	}

	private final Queue<AddMessage> queue;

	private final CalendarUtil calendarUtil;

	private final Logger logger;

	private final AnalyticsReportLogDao analyticsReportLogDao;

	@Inject
	public AddValueAction(final Logger logger, final CalendarUtil calendarUtil, final QueueBuilder queueBuilder, final AnalyticsReportLogDao analyticsReportLogDao) {
		this.logger = logger;
		this.calendarUtil = calendarUtil;
		this.analyticsReportLogDao = analyticsReportLogDao;

		queue = queueBuilder.buildQueue(new AddMessageConsumer());
	}

	public void addReportValue(final AnalyticsReportIdentifier analyticsReportIdentifier) throws AnalyticsServiceException {
		addReportValue(analyticsReportIdentifier, 1d);
	}

	public void addReportValue(final AnalyticsReportIdentifier analyticsReportIdentifier, final double value) throws AnalyticsServiceException {
		addReportValue(analyticsReportIdentifier, new AnalyticsReportValueDto(calendarUtil.now(), value, 1l));
	}

	public void addReportValue(final AnalyticsReportIdentifier analyticsReportIdentifier, final long value) throws AnalyticsServiceException {
		addReportValue(analyticsReportIdentifier, new AnalyticsReportValueDto(calendarUtil.now(), new Long(value).doubleValue(), 1l));
	}

	public void addReportValue(final AnalyticsReportIdentifier analyticsReportIdentifier, final AnalyticsReportValue reportValue) throws AnalyticsServiceException {
		logger.trace("addReportValue " + analyticsReportIdentifier);
		queue.put(new AddMessage(analyticsReportIdentifier, reportValue));
	}
}
