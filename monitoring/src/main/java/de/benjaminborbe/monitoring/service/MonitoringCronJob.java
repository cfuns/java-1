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
import de.benjaminborbe.monitoring.api.CheckResult;
import de.benjaminborbe.monitoring.check.NodeChecker;
import de.benjaminborbe.monitoring.check.RootNode;

@Singleton
public class MonitoringCronJob implements CronJob {

	/* s m h d m dw y */
	private static final String SCHEDULE_EXPRESSION = "0 */5 * * * ?";

	private final Logger logger;

	private final MailService mailService;

	private final RootNode rootNode;

	private final NodeChecker nodeChecker;

	@Inject
	public MonitoringCronJob(final Logger logger, final MailService mailService, final RootNode rootNode, final NodeChecker nodeChecker) {
		this.logger = logger;
		this.mailService = mailService;
		this.rootNode = rootNode;
		this.nodeChecker = nodeChecker;
	}

	@Override
	public String getScheduleExpression() {
		return SCHEDULE_EXPRESSION;
	}

	@Override
	public void execute() {
		logger.trace("MonitoringCronJob.execute()");
		final Collection<CheckResult> failedChecks = callChecks();

		// send mail
		if (failedChecks.size() > 0) {
			logger.trace(failedChecks.size() + " checks failed, try sending mail");
			final Mail mail = buildMail(failedChecks);
			try {
				mailService.send(mail);
			}
			catch (final MailSendException e) {
				logger.error("MailSendException", e);
			}
		}
		else {
			logger.trace("all checks past");
		}

		logger.trace("MonitoringCronJob.execute() - finished");
	}

	protected Collection<CheckResult> callChecks() {
		final Collection<CheckResult> results = nodeChecker.checkNode(rootNode);

		final Set<CheckResult> failedChecks = new HashSet<CheckResult>();
		for (final CheckResult checkResult : results) {
			try {
				if (checkResult.isSuccess()) {
					logger.trace(checkResult.toString());
				}
				else {
					logger.warn(checkResult.toString());
					failedChecks.add(checkResult);
				}
			}
			catch (final Exception e) {
				logger.warn("Check failed: " + checkResult.getCheck().getClass().getSimpleName(), e);
			}
		}
		return failedChecks;
	}

	protected Mail buildMail(final Collection<CheckResult> failedChecks) {
		final StringBuffer content = new StringBuffer();
		content.append("Checks failed: " + failedChecks.size());
		content.append("\n");
		for (final CheckResult checkResult : failedChecks) {
			content.append(checkResult.toString());
			content.append(" ");
			content.append(checkResult.getUrl());
			content.append("\n");
		}
		final String from = "bborbe@seibert-media.net";
		final String to = "bborbe@seibert-media.net";
		final String subject = "BB - Monitoring";
		return new Mail(from, to, subject, content.toString(), "text/plain");
	}
}
