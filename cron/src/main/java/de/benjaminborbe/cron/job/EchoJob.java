package de.benjaminborbe.cron.job;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
@DisallowConcurrentExecution
public class EchoJob implements Job {

	private final Logger logger;

	@Inject
	public EchoJob(final Logger logger) {
		this.logger = logger;
	}

	@Override
	public void execute(final JobExecutionContext context) throws JobExecutionException {
		logger.debug("EchoJob.execute");
	}

}
