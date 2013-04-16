package de.benjaminborbe.worktime.core.util;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.api.StorageIterator;
import de.benjaminborbe.storage.api.StorageService;
import de.benjaminborbe.storage.api.StorageValue;
import de.benjaminborbe.storage.tools.StorageValueList;
import de.benjaminborbe.storage.tools.StorageValueMap;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.util.ParseException;
import de.benjaminborbe.tools.util.ParseUtil;
import org.slf4j.Logger;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Singleton
public class WorktimeStorageServiceImpl implements WorktimeStorageService {

	private static final String FIELD_DATE = "date";

	private static final String FIELD_DATETIME = "datetime";

	private static final String FIELD_IN_OFFICE = "inOffice";

	private static final String COLUMNFAMILY = "worktime";

	private final Logger logger;

	private final StorageService storageService;

	private final CalendarUtil calendarUtil;

	private final ParseUtil parseUtil;

	private final TimeZoneUtil timeZoneUtil;

	@Inject
	public WorktimeStorageServiceImpl(
		final Logger logger,
		final StorageService storageService,
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
	public void save(final WorktimeValue worktimeValue) throws StorageException {
		logger.trace("save");
		final String dateTimeString = calendarUtil.toDateTimeString(worktimeValue.getDate());
		final String dateString = calendarUtil.toDateString(worktimeValue.getDate());
		final String inOffice = String.valueOf(worktimeValue.getInOffice());
		storageService.set(COLUMNFAMILY, new StorageValue(dateTimeString, getEncoding()),
			new StorageValueMap(getEncoding()).add(FIELD_IN_OFFICE, inOffice).add(FIELD_DATETIME, dateTimeString).add(FIELD_DATE, dateString));
	}

	private String getEncoding() {
		return storageService.getEncoding();
	}

	@Override
	public Collection<WorktimeValue> findByDate(final Calendar calendar) throws StorageException {
		logger.trace("findByDate");
		final Set<WorktimeValue> result = new HashSet<>();
		final String dateString = calendarUtil.toDateString(calendar);
		final StorageIterator i = storageService.keyIterator(COLUMNFAMILY, new StorageValueMap(getEncoding()).add(FIELD_DATE, dateString));
		while (i.hasNext()) {
			try {
				final StorageValue id = i.next();
				final List<StorageValue> list = storageService.get(COLUMNFAMILY, id, new StorageValueList(getEncoding()).add(FIELD_IN_OFFICE).add(FIELD_DATETIME));
				final String inOfficeString = list.get(0).getString();
				final String dateTimeString = list.get(1).getString();
				result.add(new WorktimeValueImpl(calendarUtil.parseDateTime(timeZoneUtil.getUTCTimeZone(), dateTimeString), parseUtil.parseBoolean(inOfficeString)));
			} catch (final ParseException | UnsupportedEncodingException e) {
				logger.error(e.getClass().getName(), e);
			}
		}
		logger.trace("found " + result.size() + " worktimeValues");
		return result;
	}
}
