package de.benjaminborbe.monitoring.service;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.cron.api.CronJob;
import de.benjaminborbe.mail.api.Mail;
import de.benjaminborbe.mail.api.MailSendException;
import de.benjaminborbe.mail.api.MailService;
import de.benjaminborbe.monitoring.check.Check;
import de.benjaminborbe.monitoring.check.CheckRegistry;

@Singleton
public class MonitoringCronJob implements CronJob {

	/* s m h d m dw y */
	private static final String SCHEDULE_EXPRESSION = "0 * * * * ?";

	private final Logger logger;

	private final CheckRegistry checkRegistry;

	private final MailService mailService;

	@Inject
	public MonitoringCronJob(final Logger logger, final CheckRegistry checkRegistry, final MailService mailService) {
		this.logger = logger;
		this.checkRegistry = checkRegistry;
		this.mailService = mailService;
	}

	@Override
	public String getScheduleExpression() {
		return SCHEDULE_EXPRESSION;
	}

	@Override
	public void execute() {
		logger.debug("MonitoringCronJob.execute()");
		final Collection<Check> failedChecks = callChecks();

		// send mail
		if (failedChecks.size() > 0) {
			logger.debug(failedChecks.size() + " checks failed, try sending mail");
			final Mail mail = buildMail(failedChecks);
			try {
				mailService.send(mail);
			}
			catch (final MailSendException e) {
				logger.error("MailSendException", e);
			}
		}
		else {
			logger.debug("all checks past");
		}

		logger.debug("MonitoringCronJob.execute() - finished");
	}

	protected Collection<Check> callChecks() {
		final Set<Check> failedChecks = new HashSet<Check>();
		for (final Check check : checkRegistry.getAll()) {
			try {
				if (check.check()) {
					logger.debug("[OK] " + check.getClass().getSimpleName());
				}
				else {
					logger.warn("[FAIL] " + check.getClass().getSimpleName());
					failedChecks.add(check);
				}
			}
			catch (final Exception e) {
				logger.warn("Check failed: " + check.getClass().getSimpleName(), e);
			}
		}
		return failedChecks;
	}

	protected Mail buildMail(final Collection<Check> failedChecks) {
		final StringBuffer content = new StringBuffer();
		for (final Check check : failedChecks) {
			content.append(check.getMessage());
			content.append("\n");
		}
		final String from = "bborbe@seibert-media.net";
		final String to = "bborbe@seibert-media.net";
		final String subject = "BB - Monitoring";
		return new Mail(from, to, subject, content.toString());
	}
}
