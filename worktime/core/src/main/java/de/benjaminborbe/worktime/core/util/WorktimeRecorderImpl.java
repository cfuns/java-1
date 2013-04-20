package de.benjaminborbe.worktime.core.util;

import javax.inject.Inject;
import javax.inject.Singleton;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.worktime.api.WorktimeRecorder;
import de.benjaminborbe.worktime.api.WorktimeRecorderException;
import org.slf4j.Logger;

import java.util.Calendar;

@Singleton
public class WorktimeRecorderImpl implements WorktimeRecorder {

	private final Logger logger;

	private final WorktimeStorageService worktimeStorageService;

	private final TimeZoneUtil timeZoneUtil;

	private final CalendarUtil calendarUtil;

	private final InOfficeCheckHttpContent inOfficeCheck;

	@Inject
	public WorktimeRecorderImpl(
		final Logger logger,
		final WorktimeStorageService worktimeStorageService,
		final TimeZoneUtil timeZoneUtil,
		final CalendarUtil calendarUtil,
		final InOfficeCheckHttpContent inOfficeCheck) {
		this.logger = logger;
		this.worktimeStorageService = worktimeStorageService;
		this.timeZoneUtil = timeZoneUtil;
		this.calendarUtil = calendarUtil;
		this.inOfficeCheck = inOfficeCheck;
	}

	@Override
	public void recordWorktime() throws WorktimeRecorderException {
		try {
			final boolean inOffice = inOfficeCheck.check();
			logger.trace("inOffice = " + inOffice);
			final Calendar calendar = calendarUtil.now(timeZoneUtil.getUTCTimeZone());
			worktimeStorageService.save(new WorktimeValueImpl(calendar, inOffice));
		} catch (final StorageException e) {
			throw new WorktimeRecorderException("StorageException", e);
		}
	}

}
