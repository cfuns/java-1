package de.benjaminborbe.monitoring.util;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;

import com.google.inject.Inject;

import de.benjaminborbe.mail.api.Mail;
import de.benjaminborbe.mail.api.MailSendException;
import de.benjaminborbe.mail.api.MailService;
import de.benjaminborbe.monitoring.api.CheckResult;
import de.benjaminborbe.monitoring.check.NodeChecker;
import de.benjaminborbe.monitoring.check.RootNode;

public class MonitoringMailer implements Runnable {

	private final Logger logger;

	private final MailService mailService;

	private final RootNode rootNode;

	private final NodeChecker nodeChecker;

	@Inject
	public MonitoringMailer(final Logger logger, final MailService mailService, final RootNode rootNode, final NodeChecker nodeChecker) {
		this.logger = logger;
		this.mailService = mailService;
		this.rootNode = rootNode;
		this.nodeChecker = nodeChecker;
	}

	@Override
	public void run() {
		logger.trace("MonitoringMailer.execute()");
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

		logger.trace("MonitoringMailer.execute() - finished");
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
					logger.trace(checkResult.toString());
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
		final String from = "bborbe@seibert-media.net";
		final String to = "bborbe@seibert-media.net";
		final String subject = "BB - Monitoring";
		return new Mail(from, to, subject, buildMailContent(failedChecks), "text/plain");
	}

	protected String buildMailContent(final Collection<CheckResult> failedChecks) {
		final StringBuffer content = new StringBuffer();
		content.append("Checks failed: " + failedChecks.size());
		content.append("\n");
		for (final CheckResult checkResult : failedChecks) {
			content.append(checkResult.getCheck().getName());
			content.append(": ");
			content.append(checkResult.getMessage());
			content.append(" ");
			content.append(checkResult.getUrl());
			content.append("\n");
		}
		content.append("\n");
		content.append("http://bb/bb/monitoring");
		content.append("\n");
		return content.toString();
	}
}
