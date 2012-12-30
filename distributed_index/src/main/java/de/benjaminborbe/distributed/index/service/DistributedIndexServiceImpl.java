package de.benjaminborbe.distributed.index.service;

import java.util.Collection;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.distributed.index.api.DistributedIndexIdentifier;
import de.benjaminborbe.distributed.index.api.DistributedIndexSearchResultIterator;
import de.benjaminborbe.distributed.index.api.DistributedIndexService;
import de.benjaminborbe.distributed.index.api.DistributedIndexServiceException;
import de.benjaminborbe.distributed.index.dao.DistributedIndexBean;
import de.benjaminborbe.distributed.index.dao.DistributedIndexDao;
import de.benjaminborbe.tools.date.CalendarUtil;

@Singleton
public class DistributedIndexServiceImpl implements DistributedIndexService {

	private final Logger logger;

	private final DistributedIndexDao distributedIndexDao;

	private final CalendarUtil calendarUtil;

	@Inject
	public DistributedIndexServiceImpl(final Logger logger, final CalendarUtil calendarUtil, final DistributedIndexDao distributedIndexDao) {
		this.logger = logger;
		this.calendarUtil = calendarUtil;
		this.distributedIndexDao = distributedIndexDao;
	}

	@Override
	public void add(final DistributedIndexIdentifier id, final Map<String, Integer> data) throws DistributedIndexServiceException {
		logger.debug("add - id: " + id);
		final DistributedIndexBean bean = distributedIndexDao.create();
		bean.setId(id);
		bean.setData(data);
		bean.setCreated(calendarUtil.now());
		bean.setModified(calendarUtil.now());
		distributedIndexDao.save(bean);
	}

	@Override
	public void remove(final DistributedIndexIdentifier id) throws DistributedIndexServiceException {
		logger.debug("remove - id: " + id);
		distributedIndexDao.remove(id);
	}

	@Override
	public DistributedIndexSearchResultIterator search(final Collection<String> words) throws DistributedIndexServiceException {
		logger.debug("search - words: " + StringUtils.join(words, ','));
		return null;
	}

}
