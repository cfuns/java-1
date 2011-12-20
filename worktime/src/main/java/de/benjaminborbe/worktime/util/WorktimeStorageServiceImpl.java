package de.benjaminborbe.worktime.util;

import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.storage.api.PersistentStorageService;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.util.ParseException;
import de.benjaminborbe.tools.util.ParseUtil;

@Singleton
public class WorktimeStorageServiceImpl implements WorktimeStorageService {

	private static final String FIELD_DATETIME = "datetime";

	private static final String FIELD_IN_OFFICE = "inOffice";

	private static final String COLUMNFAMILY = "worktime";

	private final Logger logger;

	private final PersistentStorageService storageService;

	private final CalendarUtil calendarUtil;

	private final ParseUtil parseUtil;

	private final TimeZoneUtil timeZoneUtil;

	@Inject
	public WorktimeStorageServiceImpl(
			final Logger logger,
			final PersistentStorageService storageService,
			final CalendarUtil calendarUtil,
			final TimeZoneUtil timeZoneUtil,
			final ParseUtil parseUtil) {
		this.logger = logger;
		this.storageService = storageService;
		this.calendarUtil = calendarUtil;
		this.timeZoneUtil = timeZoneUtil;
		this.parseUtil = parseUtil;
	}

	@Override
	public void save(final WorktimeValue worktimeValue) {
		logger.debug("save");
		final String dateTimeString = calendarUtil.toDateTimeString(worktimeValue.getDate());
		final String inOffice = String.valueOf(worktimeValue.getInOffice());
		storageService.set(COLUMNFAMILY, dateTimeString, FIELD_IN_OFFICE, inOffice);
		storageService.set(COLUMNFAMILY, dateTimeString, FIELD_DATETIME, dateTimeString);
	}

	@Override
	public Collection<WorktimeValue> findByDate(final Calendar calendar) {
		logger.debug("findByDate");
		final Set<WorktimeValue> result = new HashSet<WorktimeValue>();
		final String dateString = calendarUtil.toDateTimeString(calendar);
		final Collection<String> ids = storageService.findByIdPrefix(COLUMNFAMILY, dateString);
		for (final String id : ids) {
			final String inOfficeString = storageService.get(COLUMNFAMILY, id, FIELD_IN_OFFICE);
			final String dateTimeString = storageService.get(COLUMNFAMILY, id, FIELD_DATETIME);
			try {
				result.add(new WorktimeValueImpl(calendarUtil.parseDateTime(timeZoneUtil.getUTCTimeZone(), dateTimeString),
						parseUtil.parseBoolean(inOfficeString)));
			}
			catch (final ParseException e) {
				logger.error("ParseException", e);
			}
		}
		return result;
	}
}
