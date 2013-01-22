package de.benjaminborbe.monitoring.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;

import com.google.inject.Inject;

import de.benjaminborbe.mail.api.MailDto;
import de.benjaminborbe.mail.api.MailService;
import de.benjaminborbe.mail.api.MailServiceException;
import de.benjaminborbe.monitoring.api.MonitoringCheckType;
import de.benjaminborbe.monitoring.api.MonitoringNode;
import de.benjaminborbe.monitoring.check.MonitoringCheck;
import de.benjaminborbe.monitoring.check.MonitoringCheckFactory;
import de.benjaminborbe.monitoring.dao.MonitoringNodeBean;
import de.benjaminborbe.monitoring.dao.MonitoringNodeDao;
import de.benjaminborbe.monitoring.tools.MonitoringNodeComparator;
import de.benjaminborbe.monitoring.tools.MonitoringNodeTree;
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

	private final MonitoringNodeBuilder monitoringNodeBuilder;

	private static final int FAILURE_COUNTER_LIMIT = 3;

	private final class Action implements Runnable {

		@Override
		public void run() {
			try {

				final List<MonitoringNode> nodes = new ArrayList<MonitoringNode>();

				final EntityIterator<MonitoringNodeBean> i = monitoringNodeDao.getEntityIterator();
				while (i.hasNext()) {
					final MonitoringNodeBean bean = i.next();
					nodes.add(monitoringNodeBuilder.build(bean));
				}

				final MonitoringNodeTree<MonitoringNode> tree = new MonitoringNodeTree<MonitoringNode>(nodes);
				final List<MonitoringNode> results = new ArrayList<MonitoringNode>();
				handle(results, tree.getRootNodes(), tree);
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

		private void handle(final List<MonitoringNode> results, final List<MonitoringNode> list, final MonitoringNodeTree<MonitoringNode> tree) {
			Collections.sort(list, new MonitoringNodeComparator<MonitoringNode>());
			for (final MonitoringNode node : list) {
				final String label = buildLabel(node);
				if (Boolean.TRUE.equals(node.getActive())) {
					logger.debug("node is active " + label);
					if (Boolean.FALSE.equals(node.getResult())) {
						if (Boolean.FALSE.equals(node.getSilent())) {
							if (node.getFailureCounter() != null && node.getFailureCounter() >= FAILURE_COUNTER_LIMIT) {
								results.add(node);
								logger.debug("node " + label + " has failure => add");
							}
							else {
								logger.debug("failureCounter(" + node.getFailureCounter() + ") < limit(" + FAILURE_COUNTER_LIMIT + ") => skip");
							}
						}
						else {
							logger.debug("node " + label + " is silent => skip");
						}
					}
					else {
						logger.debug("node " + label + " is success => skip");
						handle(results, tree.getChildNodes(node.getId()), tree);
					}
				}
				else {
					logger.debug("node is inactive " + label);
				}
			}
		}
	}

	@Inject
	public MonitoringMailer(
			final Logger logger,
			final MonitoringNodeBuilder monitoringNodeBuilder,
			final MailService mailService,
			final RunOnlyOnceATime runOnlyOnceATime,
			final MonitoringNodeDao monitoringNodeDao,
			final MonitoringCheckFactory monitoringCheckFactory) {
		this.logger = logger;
		this.monitoringNodeBuilder = monitoringNodeBuilder;
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

	private MailDto buildMail(final List<MonitoringNode> results) {
		final String from = "bborbe@seibert-media.net";
		final String to = "bborbe@seibert-media.net";
		final String subject = "BB - Monitoring";
		return new MailDto(from, to, subject, buildMailContent(results), "text/plain");
	}

	private String buildMailContent(final List<MonitoringNode> results) {
		final StringBuffer content = new StringBuffer();
		content.append("Checks failed: " + results.size());
		content.append("\n");
		for (final MonitoringNode bean : results) {
			content.append(buildLabel(bean));
			content.append(": ");
			content.append(bean.getMessage());
			content.append("\n");
		}
		content.append("\n");
		content.append("http://bb/bb/monitoring");
		content.append("\n");
		return content.toString();
	}

	private String buildLabel(final MonitoringNode bean) {
		final MonitoringCheckType type = bean.getCheckType();
		final MonitoringCheck check = monitoringCheckFactory.get(type);
		return check.getDescription(bean.getParameter()) + " (" + bean.getName() + ")";
	}
}
