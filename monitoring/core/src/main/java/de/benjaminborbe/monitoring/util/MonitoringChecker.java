package de.benjaminborbe.monitoring.util;

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
import org.slf4j.Logger;

import javax.inject.Inject;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class MonitoringChecker {

	private final RunOnlyOnceATime runOnlyOnceATime;

	private final Logger logger;

	private final MonitoringNodeDao monitoringNodeDao;

	private final MonitoringCheckRegistry monitoringCheckRegistry;

	private final CalendarUtil calendarUtil;

	@Inject
	public MonitoringChecker(
		final Logger logger,
		final CalendarUtil calendarUtil,
		final RunOnlyOnceATime runOnlyOnceATime,
		final MonitoringNodeDao monitoringNodeDao,
		final MonitoringCheckRegistry monitoringCheckRegistry
	) {
		this.logger = logger;
		this.calendarUtil = calendarUtil;
		this.runOnlyOnceATime = runOnlyOnceATime;
		this.monitoringNodeDao = monitoringNodeDao;
		this.monitoringCheckRegistry = monitoringCheckRegistry;
	}

	public boolean checkAll() {
		logger.debug("monitoring checker - started");
		if (runOnlyOnceATime.run(new Action())) {
			logger.debug("monitoring checker - finished");
			return true;
		} else {
			logger.debug("monitoring checker - skipped");
			return false;
		}
	}

	public void check(final MonitoringNodeBean node) throws StorageException {
		final Collection<MonitoringNodeBean> a = Arrays.asList();
		check(node, new MonitoringNodeTree<MonitoringNodeBean>(a));
	}

	private void check(final List<MonitoringNodeBean> nodes, final MonitoringNodeTree<MonitoringNodeBean> tree) throws StorageException {
		for (final MonitoringNodeBean bean : nodes) {
			check(bean, tree);
		}
	}

	private void check(final MonitoringNodeBean node, final MonitoringNodeTree<MonitoringNodeBean> tree) throws StorageException {
		final MonitoringCheckIdentifier type = node.getCheckType();
		final MonitoringCheck check = monitoringCheckRegistry.get(type);
		if (check == null) {
			logger.warn("no check found for type: " + type);
			return;
		}
		final String name = check.getDescription(node.getParameter()) + " (" + node.getName() + ")";
		if (Boolean.TRUE.equals(node.getActive())) {
			logger.debug("node " + name + " active => run check");
			final MonitoringCheckResult result = check.check(node.getParameter());
			node.setLastCheck(calendarUtil.now());
			if (Boolean.TRUE.equals(result.getSuccessful())) {
				logger.debug("node " + name + " success");

				node.setMessage(result.getMessage());
				node.setResult(result.getSuccessful());
				node.setFailureCounter(0);
				node.setException(null);
				save(node);

				check(tree.getChildNodes(node.getId()), tree);
			} else if (Boolean.FALSE.equals(result.getSuccessful())) {
				if (result.getException() != null) {
					logger.debug("node " + name + " fail: " + result.getMessage(), result.getException());
				} else {
					logger.debug("node " + name + " fail: " + result.getMessage());
				}

				node.setMessage(result.getMessage());
				node.setResult(result.getSuccessful());
				node.setFailureCounter(node.getFailureCounter() != null ? node.getFailureCounter() + 1 : 1);
				node.setException(asString(result.getException()));
				save(node);

				reset(tree.getChildNodes(node.getId()), tree);
			} else {
				logger.debug("node " + name + " unkown");

				node.setMessage(result.getMessage());
				node.setResult(result.getSuccessful());
				node.setException(asString(result.getException()));
				save(node);

				reset(tree.getChildNodes(node.getId()), tree);
			}
		} else {
			logger.debug("node " + name + " inactive => skip");
			reset(node);
		}
	}

	private String asString(final Exception exception) {
		if (exception == null) {
			return null;
		}
		final StringWriter sw = new StringWriter();
		exception.printStackTrace(new PrintWriter(sw));
		return sw.toString();
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
		bean.setException(null);
		save(bean);
	}

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
			} catch (final StorageException e) {
				logger.warn(e.getClass().getName(), e);
			} catch (EntityIteratorException e) {
				logger.warn(e.getClass().getName(), e);
			}
		}
	}
}
