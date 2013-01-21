package de.benjaminborbe.monitoring.util;

import org.slf4j.Logger;

import com.google.inject.Inject;

import de.benjaminborbe.monitoring.api.MonitoringCheckType;
import de.benjaminborbe.monitoring.check.MonitoringCheck;
import de.benjaminborbe.monitoring.check.MonitoringCheckFactory;
import de.benjaminborbe.monitoring.check.MonitoringCheckResult;
import de.benjaminborbe.monitoring.dao.MonitoringNodeBean;
import de.benjaminborbe.monitoring.dao.MonitoringNodeDao;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.tools.EntityIterator;
import de.benjaminborbe.storage.tools.EntityIteratorException;
import de.benjaminborbe.tools.synchronize.RunOnlyOnceATime;

public class MonitoringChecker {

	private final RunOnlyOnceATime runOnlyOnceATime;

	private final Logger logger;

	private final MonitoringNodeDao monitoringNodeDao;

	private final MonitoringCheckFactory monitoringCheckFactory;

	private final class Action implements Runnable {

		@Override
		public void run() {
			try {
				final EntityIterator<MonitoringNodeBean> i = monitoringNodeDao.getEntityIterator();
				while (i.hasNext()) {
					final MonitoringNodeBean bean = i.next();
					if (Boolean.TRUE.equals(bean.getActive())) {
						logger.debug("node " + bean.getName() + " active => run check");
						final MonitoringCheckType type = bean.getCheckType();
						final MonitoringCheck check = monitoringCheckFactory.get(type);
						final MonitoringCheckResult result = check.check(bean.getParameter());
						if (result.isSuccessful()) {
							logger.debug("node " + bean.getName() + " success");
						}
						else {
							logger.debug("node " + bean.getName() + " fail");
						}
					}
					else {
						logger.debug("node inactive => skip");
					}
				}
			}
			catch (final EntityIteratorException e) {
				logger.warn(e.getClass().getName(), e);
			}
			catch (final StorageException e) {
				logger.warn(e.getClass().getName(), e);
			}
		}
	}

	@Inject
	public MonitoringChecker(
			final Logger logger,
			final RunOnlyOnceATime runOnlyOnceATime,
			final MonitoringNodeDao monitoringNodeDao,
			final MonitoringCheckFactory monitoringCheckFactory) {
		this.logger = logger;
		this.runOnlyOnceATime = runOnlyOnceATime;
		this.monitoringNodeDao = monitoringNodeDao;
		this.monitoringCheckFactory = monitoringCheckFactory;
	}

	public boolean check() {
		logger.debug("monitoring checker - started");
		if (runOnlyOnceATime.run(new Action())) {
			logger.debug("monitoring checker - finished");
			return true;
		}
		else {
			logger.debug("monitoring checker - skipped");
			return false;
		}
	}
}
