package de.benjaminborbe.monitoring.util;

import javax.inject.Inject;
import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.AuthorizationServiceException;
import de.benjaminborbe.authorization.api.RoleIdentifier;
import de.benjaminborbe.monitoring.MonitoringConstants;
import de.benjaminborbe.monitoring.api.MonitoringCheck;
import de.benjaminborbe.monitoring.api.MonitoringCheckIdentifier;
import de.benjaminborbe.monitoring.api.MonitoringNode;
import de.benjaminborbe.monitoring.check.MonitoringCheckRegistry;
import de.benjaminborbe.monitoring.dao.MonitoringNodeBean;
import de.benjaminborbe.monitoring.dao.MonitoringNodeDao;
import de.benjaminborbe.monitoring.tools.MonitoringNodeComparator;
import de.benjaminborbe.monitoring.tools.MonitoringNodeTree;
import de.benjaminborbe.notification.api.NotificationDto;
import de.benjaminborbe.notification.api.NotificationService;
import de.benjaminborbe.notification.api.NotificationServiceException;
import de.benjaminborbe.notification.api.NotificationTypeIdentifier;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.tools.EntityIterator;
import de.benjaminborbe.storage.tools.EntityIteratorException;
import de.benjaminborbe.tools.synchronize.RunOnlyOnceATime;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class MonitoringNotifier {

	private final RunOnlyOnceATime runOnlyOnceATime;

	private final Logger logger;

	private final MonitoringNodeDao monitoringNodeDao;

	private final MonitoringCheckRegistry monitoringCheckRegistry;

	private final NotificationService notificationService;

	private final MonitoringNodeBuilder monitoringNodeBuilder;

	private static final int FAILURE_COUNTER_LIMIT = 3;

	private final AuthorizationService authorizationService;

	private static final NotificationTypeIdentifier TYPE = new NotificationTypeIdentifier("monitoring");

	private final class Action implements Runnable {

		@Override
		public void run() {
			try {
				final List<MonitoringNode> nodes = new ArrayList<>();

				final EntityIterator<MonitoringNodeBean> i = monitoringNodeDao.getEntityIterator();
				while (i.hasNext()) {
					final MonitoringNodeBean bean = i.next();
					nodes.add(monitoringNodeBuilder.build(bean));
				}

				final MonitoringNodeTree<MonitoringNode> tree = new MonitoringNodeTree<>(nodes);
				final List<MonitoringNode> results = new ArrayList<>();
				handle(results, tree.getRootNodes(), tree);
				if (results.isEmpty()) {
					logger.debug("no errors found => skip mail");
				} else {
					final RoleIdentifier roleIdentifier = authorizationService.createRoleIdentifier(MonitoringConstants.ROLE_MONITORING_SUPERVISOR);
					final Collection<UserIdentifier> userIdentifiers = authorizationService.getUsersWithRole(roleIdentifier);
					for (final UserIdentifier userIdentifier : userIdentifiers) {
						final String subject = "BB - Monitoring";
						final String message = buildContent(results);
						try {
							final NotificationDto notification = new NotificationDto();
							notification.setTo(userIdentifier);
							notification.setType(TYPE);
							notification.setSubject(subject);
							notification.setMessage(message);
							notificationService.notify(notification);
						} catch (NotificationServiceException | ValidationException e) {
							logger.warn("notify user failed", e);
						}
					}
				}
			} catch (final EntityIteratorException | AuthorizationServiceException | StorageException e) {
				logger.warn("notify failed", e);
			}
		}

		private void handle(final List<MonitoringNode> results, final List<MonitoringNode> list, final MonitoringNodeTree<MonitoringNode> tree) {
			Collections.sort(list, new MonitoringNodeComparator<>());
			for (final MonitoringNode node : list) {
				final String label = buildLabel(node);
				if (Boolean.TRUE.equals(node.getActive())) {
					logger.debug("node is active " + label);
					if (Boolean.FALSE.equals(node.getResult())) {
						if (Boolean.FALSE.equals(node.getSilent())) {
							if (node.getFailureCounter() != null && node.getFailureCounter() >= FAILURE_COUNTER_LIMIT) {
								results.add(node);
								logger.debug("node " + label + " has failure => add");
							} else {
								logger.debug("failureCounter(" + node.getFailureCounter() + ") < limit(" + FAILURE_COUNTER_LIMIT + ") => skip");
							}
						} else {
							logger.debug("node " + label + " is silent => skip");
						}
					} else {
						logger.debug("node " + label + " is success => skip");
						handle(results, tree.getChildNodes(node.getId()), tree);
					}
				} else {
					logger.debug("node is inactive " + label);
				}
			}
		}
	}

	@Inject
	public MonitoringNotifier(
		final Logger logger,
		final AuthorizationService authorizationService,
		final MonitoringNodeBuilder monitoringNodeBuilder,
		final NotificationService notificationService,
		final RunOnlyOnceATime runOnlyOnceATime,
		final MonitoringNodeDao monitoringNodeDao,
		final MonitoringCheckRegistry monitoringCheckRegistry) {
		this.logger = logger;
		this.authorizationService = authorizationService;
		this.monitoringNodeBuilder = monitoringNodeBuilder;
		this.notificationService = notificationService;
		this.runOnlyOnceATime = runOnlyOnceATime;
		this.monitoringNodeDao = monitoringNodeDao;
		this.monitoringCheckRegistry = monitoringCheckRegistry;
	}

	public boolean mail() {
		logger.debug("monitoring mailer - started");
		if (runOnlyOnceATime.run(new Action())) {
			logger.debug("monitoring mailer - finished");
			return true;
		} else {
			logger.debug("monitoring mailer - skipped");
			return false;
		}
	}

	private String buildContent(final List<MonitoringNode> results) {
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
		final MonitoringCheckIdentifier type = bean.getCheckType();
		final MonitoringCheck check = monitoringCheckRegistry.get(type);
		return check.getDescription(bean.getParameter()) + " (" + bean.getName() + ")";
	}
}
