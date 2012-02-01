package de.benjaminborbe.worktime.service;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.cron.api.CronJob;
import de.benjaminborbe.worktime.api.WorktimeRecorder;
import de.benjaminborbe.worktime.api.WorktimeRecorderException;

@Singleton
public class WorktimeCronJob implements CronJob {

	/* s m h d m dw y */
	private static final String SCHEDULE_EXPRESSION = "0 * * * * ?";

	private final Logger logger;

	private final WorktimeRecorder worktimeRecorder;

	@Inject
	public WorktimeCronJob(final Logger logger, final WorktimeRecorder worktimeRecorder) {
		this.logger = logger;
		this.worktimeRecorder = worktimeRecorder;
	}

	@Override
	public String getScheduleExpression() {
		return SCHEDULE_EXPRESSION;
	}

	@Override
	public void execute() {
		logger.trace("execute WorktimeCronJob");
		try {
			worktimeRecorder.recordWorktime();
		}
		catch (final WorktimeRecorderException e) {
			logger.error("WorktimeRecorderException", e);
		}
	}

}
