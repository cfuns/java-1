package de.benjaminborbe.monitoring.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;

import com.google.inject.Inject;

import de.benjaminborbe.monitoring.api.MonitoringCheck;
import de.benjaminborbe.monitoring.api.MonitoringCheckIdentifier;
import de.benjaminborbe.monitoring.api.MonitoringCheckResult;
import de.benjaminborbe.monitoring.check.MonitoringCheckRegistry;
import de.benjaminborbe.monitoring.dao.MonitoringNodeBean;
import de.benjaminborbe.monitoring.dao.MonitoringNodeBeanMapper;
import de.benjaminborbe.monitoring.dao.MonitoringNodeDao;
import de.benjaminborbe.monitoring.tools.MonitoringNodeTree;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.tools.EntityIterator;
import de.benjaminborbe.storage.tools.EntityIteratorException;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.synchronize.RunOnlyOnceATime;

public class MonitoringChecker {

	private final RunOnlyOnceATime runOnlyOnceATime;

	private final Logger logger;

	private final MonitoringNodeDao monitoringNodeDao;

	private final MonitoringCheckRegistry monitoringCheckRegistry;

	private final CalendarUtil calendarUtil;

	private final class Action implements Runnable {

		@Override
		public void run() {
			try {
				final List<MonitoringNodeBean> beans = new ArrayList<MonitoringNodeBean>();
				final EntityIterator<MonitoringNodeBean> i = monitoringNodeDao.getEntityIterator();
				while (i.hasNext()) {
					final MonitoringNodeBean bean = i.next();
					beans.add(bean);
				}
				final MonitoringNodeTree<MonitoringNodeBean> tree = new MonitoringNodeTree<MonitoringNodeBean>(beans);
				check(tree.getRootNodes(), tree);
			}
			catch (final EntityIteratorException e) {
				logger.warn(e.getClass().getName(), e);
			}
			catch (final StorageException e) {
				logger.warn(e.getClass().getName(), e);
			}
		}

		private void check(final List<MonitoringNodeBean> nodes, final MonitoringNodeTree<MonitoringNodeBean> tree) throws StorageException {
			for (final MonitoringNodeBean bean : nodes) {
				final MonitoringCheckIdentifier type = bean.getCheckType();
				final MonitoringCheck check = monitoringCheckRegistry.get(type);
				final String name = check.getDescription(bean.getParameter()) + " (" + bean.getName() + ")";
				if (Boolean.TRUE.equals(bean.getActive())) {
					logger.debug("node " + name + " active => run check");
					final MonitoringCheckResult result = check.check(bean.getParameter());
					bean.setLastCheck(calendarUtil.now());
					if (Boolean.TRUE.equals(result.getSuccessful())) {
						logger.debug("node " + name + " success");

						bean.setMessage(result.getMessage());
						bean.setResult(result.getSuccessful());
						bean.setFailureCounter(0);
						save(bean);

						check(tree.getChildNodes(bean.getId()), tree);
					}
					else if (Boolean.FALSE.equals(result.getSuccessful())) {
						if (result.getException() != null) {
							logger.debug("node " + name + " fail: " + result.getMessage(), result.getException());
						}
						else {
							logger.debug("node " + name + " fail: " + result.getMessage());
						}

						bean.setMessage(result.getMessage());
						bean.setResult(result.getSuccessful());
						bean.setFailureCounter(bean.getFailureCounter() != null ? bean.getFailureCounter() + 1 : 1);
						save(bean);

						reset(tree.getChildNodes(bean.getId()), tree);
					}
					else {
						logger.debug("node " + name + " unkown");

						bean.setMessage(result.getMessage());
						bean.setResult(result.getSuccessful());
						save(bean);

						reset(tree.getChildNodes(bean.getId()), tree);
					}
				}
				else {
					logger.debug("node " + name + " inactive => skip");
					reset(bean);
				}
			}
		}

		private void save(final MonitoringNodeBean bean) throws StorageException {
			monitoringNodeDao.save(
					bean,
					Arrays.asList(monitoringNodeDao.buildValue(MonitoringNodeBeanMapper.MESSAGE), monitoringNodeDao.buildValue(MonitoringNodeBeanMapper.RESULT),
							monitoringNodeDao.buildValue(MonitoringNodeBeanMapper.LAST_CHECK), monitoringNodeDao.buildValue(MonitoringNodeBeanMapper.FAILURE_COUNTER)));
		}

		private void reset(final List<MonitoringNodeBean> nodes, final MonitoringNodeTree<MonitoringNodeBean> tree) throws StorageException {
			for (final MonitoringNodeBean bean : nodes) {
				reset(bean);
				reset(tree.getChildNodes(bean.getId()), tree);
			}
		}

		private void reset(final MonitoringNodeBean bean) throws StorageException {
			bean.setMessage(null);
			bean.setResult(null);
			bean.setFailureCounter(null);
			save(bean);
		}
	}

	@Inject
	public MonitoringChecker(
			final Logger logger,
			final CalendarUtil calendarUtil,
			final RunOnlyOnceATime runOnlyOnceATime,
			final MonitoringNodeDao monitoringNodeDao,
			final MonitoringCheckRegistry monitoringCheckRegistry) {
		this.logger = logger;
		this.calendarUtil = calendarUtil;
		this.runOnlyOnceATime = runOnlyOnceATime;
		this.monitoringNodeDao = monitoringNodeDao;
		this.monitoringCheckRegistry = monitoringCheckRegistry;
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
