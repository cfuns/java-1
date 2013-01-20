package de.benjaminborbe.monitoring.util;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Provider;

import de.benjaminborbe.mail.api.MailDto;
import de.benjaminborbe.mail.api.MailService;
import de.benjaminborbe.mail.api.MailServiceException;
import de.benjaminborbe.monitoring.api.MonitoringCheckResult;
import de.benjaminborbe.monitoring.check.NodeChecker;
import de.benjaminborbe.monitoring.check.RootNode;

public class MonitoringMailer implements Runnable {

	private final Logger logger;

	private final MailService mailService;

	private final Provider<RootNode> rootNodeProvider;

	private final NodeChecker nodeChecker;

	@Inject
	public MonitoringMailer(final Logger logger, final MailService mailService, final Provider<RootNode> rootNodeProvider, final NodeChecker nodeChecker) {
		this.logger = logger;
		this.mailService = mailService;
		this.rootNodeProvider = rootNodeProvider;
		this.nodeChecker = nodeChecker;
	}

	@Override
	public void run() {
		logger.trace("MonitoringMailer.execute()");
		final Collection<MonitoringCheckResult> failedChecks = callChecks();

		// send mail
		if (failedChecks.size() > 0) {
			logger.trace(failedChecks.size() + " checks failed, try sending mail");
			final MailDto mail = buildMail(failedChecks);
			try {
				mailService.send(mail);
			}
			catch (final MailServiceException e) {
				logger.error("MailSendException", e);
			}
		}
		else {
			logger.trace("all checks past");
		}

		logger.trace("MonitoringMailer.execute() - finished");
	}

	protected Collection<MonitoringCheckResult> callChecks() {
		final Collection<MonitoringCheckResult> results = nodeChecker.checkNode(rootNodeProvider.get());

		final Set<MonitoringCheckResult> failedChecks = new HashSet<MonitoringCheckResult>();
		for (final MonitoringCheckResult checkResult : results) {
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

	protected MailDto buildMail(final Collection<MonitoringCheckResult> failedChecks) {
		final String from = "bborbe@seibert-media.net";
		final String to = "bborbe@seibert-media.net";
		final String subject = "BB - Monitoring";
		return new MailDto(from, to, subject, buildMailContent(failedChecks), "text/plain");
	}

	protected String buildMailContent(final Collection<MonitoringCheckResult> failedChecks) {
		final StringBuffer content = new StringBuffer();
		content.append("Checks failed: " + failedChecks.size());
		content.append("\n");
		for (final MonitoringCheckResult checkResult : failedChecks) {
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
