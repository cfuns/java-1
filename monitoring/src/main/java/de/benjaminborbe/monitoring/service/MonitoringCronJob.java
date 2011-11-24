package de.benjaminborbe.monitoring.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.cron.api.CronJob;
import de.benjaminborbe.tools.util.HttpDownloadResult;
import de.benjaminborbe.tools.util.HttpDownloader;

@Singleton
public class MonitoringCronJob implements CronJob {

	/* s m h d m dw y */
	private static final String SCHEDULE_EXPRESSION = "0 * * * * ?";

	// 5 sec
	private static final int TIMEOUT = 5 * 1000;

	private final Logger logger;

	private final HttpDownloader httpDownloader;

	@Inject
	public MonitoringCronJob(final Logger logger, final HttpDownloader httpDownloader) {
		this.logger = logger;
		this.httpDownloader = httpDownloader;
	}

	@Override
	public String getScheduleExpression() {
		return SCHEDULE_EXPRESSION;
	}

	@Override
	public void execute() {
		logger.debug("MonitoringCronJob.execute()");
		try {
			final URL url = new URL("https://test.twentyfeet.com/app");
			final HttpDownloadResult result = httpDownloader.downloadUrlUnsecure(url, TIMEOUT);
			logger.debug("MonitoringCronJob.execute() - downloaded " + url + " in " + result.getDuration() + " ms");
		}
		catch (final MalformedURLException e) {
			logger.error("MonitoringCronJob.execute() - MalformedURLException", e);
		}
		catch (final IOException e) {
			logger.error("MonitoringCronJob.execute() - IOException", e);
		}
	}
}
