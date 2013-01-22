package de.benjaminborbe.monitoring.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;

import com.google.inject.Inject;

import de.benjaminborbe.monitoring.api.MonitoringCheckType;
import de.benjaminborbe.monitoring.check.MonitoringCheck;
import de.benjaminborbe.monitoring.check.MonitoringCheckFactory;
import de.benjaminborbe.monitoring.check.MonitoringCheckResult;
import de.benjaminborbe.monitoring.dao.MonitoringNodeBean;
import de.benjaminborbe.monitoring.dao.MonitoringNodeBeanMapper;
import de.benjaminborbe.monitoring.dao.MonitoringNodeDao;
import de.benjaminborbe.monitoring.tools.MonitoringNodeTree;
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
				final MonitoringCheckType type = bean.getCheckType();
				final MonitoringCheck check = monitoringCheckFactory.get(type);
				final String name = check.getDescription(bean.getParameter()) + " (" + bean.getName() + ")";
				if (Boolean.TRUE.equals(bean.getActive())) {
					logger.debug("node " + name + " active => run check");
					final MonitoringCheckResult result = check.check(bean.getParameter());
					bean.setMessage(result.getMessage());
					bean.setResult(result.getSuccessful());
					monitoringNodeDao
							.save(bean, Arrays.asList(monitoringNodeDao.buildValue(MonitoringNodeBeanMapper.MESSAGE), monitoringNodeDao.buildValue(MonitoringNodeBeanMapper.RESULT)));
					if (Boolean.TRUE.equals(result.getSuccessful())) {
						logger.debug("node " + name + " success");
						check(tree.getChildNodes(bean.getId()), tree);
					}
					else if (Boolean.FALSE.equals(result.getSuccessful())) {
						if (result.getException() != null) {
							logger.debug("node " + name + " fail: " + result.getMessage(), result.getException());
						}
						else {
							logger.debug("node " + name + " fail: " + result.getMessage());
						}
						reset(tree.getChildNodes(bean.getId()), tree);
					}
					else {
						logger.debug("node " + name + " unkown");
						reset(tree.getChildNodes(bean.getId()), tree);
					}
				}
				else {
					logger.debug("node " + name + " inactive => skip");
					reset(bean);
				}
			}
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
			monitoringNodeDao.save(bean, Arrays.asList(monitoringNodeDao.buildValue(MonitoringNodeBeanMapper.MESSAGE), monitoringNodeDao.buildValue(MonitoringNodeBeanMapper.RESULT)));
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
