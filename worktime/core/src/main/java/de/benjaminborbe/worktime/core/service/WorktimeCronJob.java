package de.benjaminborbe.worktime.core.service;

import de.benjaminborbe.cron.api.CronJob;
import de.benjaminborbe.worktime.api.WorktimeRecorder;
import de.benjaminborbe.worktime.api.WorktimeRecorderException;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;

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
		try {
			logger.debug("worktime cron => started");
			worktimeRecorder.recordWorktime();
			logger.debug("worktime cron => finished");
		} catch (final WorktimeRecorderException e) {
			logger.debug("worktime cron => skipped");
		}
	}

	@Override
	public boolean disallowConcurrentExecution() {
		return true;
	}

}
