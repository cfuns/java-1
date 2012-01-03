package de.benjaminborbe.worktime.util;

import java.util.Calendar;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.worktime.api.WorktimeRecorder;

@Singleton
public class WorktimeRecorderImpl implements WorktimeRecorder {

	private final Logger logger;

	private final WorktimeStorageService worktimeStorageService;

	private final TimeZoneUtil timeZoneUtil;

	private final CalendarUtil calendarUtil;

	private final InOfficeCheck inOfficeCheck;

	@Inject
	public WorktimeRecorderImpl(
			final Logger logger,
			final WorktimeStorageService worktimeStorageService,
			final TimeZoneUtil timeZoneUtil,
			final CalendarUtil calendarUtil,
			final InOfficeCheck inOfficeCheck) {
		this.logger = logger;
		this.worktimeStorageService = worktimeStorageService;
		this.timeZoneUtil = timeZoneUtil;
		this.calendarUtil = calendarUtil;
		this.inOfficeCheck = inOfficeCheck;
	}

	@Override
	public void recordWorktime() throws StorageException {
		final boolean inOffice = inOfficeCheck.check();
		logger.debug("inOffice = " + inOffice);
		final Calendar calendar = calendarUtil.now(timeZoneUtil.getUTCTimeZone());
		worktimeStorageService.save(new WorktimeValueImpl(calendar, inOffice));
	}

}
