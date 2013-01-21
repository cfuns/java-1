package de.benjaminborbe.monitoring.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;

import com.google.inject.Inject;

import de.benjaminborbe.mail.api.MailDto;
import de.benjaminborbe.mail.api.MailService;
import de.benjaminborbe.mail.api.MailServiceException;
import de.benjaminborbe.monitoring.api.MonitoringCheckType;
import de.benjaminborbe.monitoring.check.MonitoringCheck;
import de.benjaminborbe.monitoring.check.MonitoringCheckFactory;
import de.benjaminborbe.monitoring.dao.MonitoringNodeBean;
import de.benjaminborbe.monitoring.dao.MonitoringNodeDao;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.tools.EntityIterator;
import de.benjaminborbe.storage.tools.EntityIteratorException;
import de.benjaminborbe.tools.synchronize.RunOnlyOnceATime;

public class MonitoringMailer {

	private final RunOnlyOnceATime runOnlyOnceATime;

	private final Logger logger;

	private final MonitoringNodeDao monitoringNodeDao;

	private final MonitoringCheckFactory monitoringCheckFactory;

	private final MailService mailService;

	private final class Action implements Runnable {

		@Override
		public void run() {
			try {
				final List<MonitoringNodeBean> results = new ArrayList<MonitoringNodeBean>();
				final EntityIterator<MonitoringNodeBean> i = monitoringNodeDao.getEntityIterator();
				while (i.hasNext()) {
					final MonitoringNodeBean bean = i.next();
					if (Boolean.TRUE.equals(bean.getActive()) && Boolean.FALSE.equals(bean.getSilent()) && Boolean.FALSE.equals(bean.getResult())) {
						results.add(bean);
					}
				}
				if (results.isEmpty()) {
					logger.debug("no errors found => skip mail");
				}
				else {
					final MailDto mail = buildMail(results);
					mailService.send(mail);
				}
			}
			catch (final EntityIteratorException e) {
				logger.warn(e.getClass().getName(), e);
			}
			catch (final StorageException e) {
				logger.warn(e.getClass().getName(), e);
			}
			catch (final MailServiceException e) {
				logger.warn(e.getClass().getName(), e);
			}
		}
	}

	@Inject
	public MonitoringMailer(
			final Logger logger,
			final MailService mailService,
			final RunOnlyOnceATime runOnlyOnceATime,
			final MonitoringNodeDao monitoringNodeDao,
			final MonitoringCheckFactory monitoringCheckFactory) {
		this.logger = logger;
		this.mailService = mailService;
		this.runOnlyOnceATime = runOnlyOnceATime;
		this.monitoringNodeDao = monitoringNodeDao;
		this.monitoringCheckFactory = monitoringCheckFactory;
	}

	public boolean mail() {
		logger.debug("monitoring mailer - started");
		if (runOnlyOnceATime.run(new Action())) {
			logger.debug("monitoring mailer - finished");
			return true;
		}
		else {
			logger.debug("monitoring mailer - skipped");
			return false;
		}
	}

	private MailDto buildMail(final Collection<MonitoringNodeBean> failedChecks) {
		final String from = "bborbe@seibert-media.net";
		final String to = "bborbe@seibert-media.net";
		final String subject = "BB - Monitoring";
		return new MailDto(from, to, subject, buildMailContent(failedChecks), "text/plain");
	}

	private String buildMailContent(final Collection<MonitoringNodeBean> failedChecks) {
		final StringBuffer content = new StringBuffer();
		content.append("Checks failed: " + failedChecks.size());
		content.append("\n");
		for (final MonitoringNodeBean bean : failedChecks) {
			final MonitoringCheckType type = bean.getCheckType();
			final MonitoringCheck check = monitoringCheckFactory.get(type);
			final String name = check.getDescription(bean.getParameter()) + " (" + bean.getName() + ")";
			content.append(name);
			content.append(": ");
			content.append(bean.getMessage());
			content.append("\n");
		}
		content.append("\n");
		content.append("http://bb/bb/monitoring");
		content.append("\n");
		return content.toString();
	}
}
