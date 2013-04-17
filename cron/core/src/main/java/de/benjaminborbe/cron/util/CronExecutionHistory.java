package de.benjaminborbe.cron.util;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import de.benjaminborbe.cron.api.CronExecutionInfo;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.fifo.Fifo;
import de.benjaminborbe.tools.fifo.FifoIndexOutOfBoundsException;
import de.benjaminborbe.tools.util.MathUtil;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

@Singleton
public class CronExecutionHistory {

	private final int MAX_SIZE = 100;

	private final Fifo<CronExecutionInfo> infos = new Fifo<>();

	private final CalendarUtil calendarUtil;

	private final TimeZoneUtil timeZoneUtil;

	private final Logger logger;

	private final MathUtil mathUtil;

	@Inject
	public CronExecutionHistory(final Logger logger, final CalendarUtil calendarUtil, final TimeZoneUtil timeZoneUtil, final MathUtil mathUtil) {
		this.logger = logger;
		this.calendarUtil = calendarUtil;
		this.timeZoneUtil = timeZoneUtil;
		this.mathUtil = mathUtil;
	}

	public List<CronExecutionInfo> getLatestElements(final int amount) {
		logger.debug("getLatestElements amount = " + amount);
		final int count = mathUtil.minInteger(amount, infos.size());
		logger.debug("getLatestElements count = " + count);
		try {
			final List<CronExecutionInfo> result = infos.last(count);
			logger.debug("getLatestElements found " + result.size());
			return result;
		} catch (final FifoIndexOutOfBoundsException e) {
			logger.debug("FifoIndexOutOfBoundsException", e);
			return new ArrayList<>();
		}
	}

	public void add(final String name) {
		if (infos.size() > MAX_SIZE) {
			try {
				infos.remove();
			} catch (final FifoIndexOutOfBoundsException e) {
				logger.trace("FifoIndexOutOfBoundsException", e);
			}
		}
		infos.add(buildInfo(name));
	}

	protected CronExecutionInfo buildInfo(final String name) {
		final TimeZone timeZone = timeZoneUtil.getUTCTimeZone();
		return new CronExecutionInfo(name, calendarUtil.now(timeZone));
	}
}
