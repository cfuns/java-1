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
import de.benjaminborbe.distributed.index.dao.DistributedIndexEntryBean;
import de.benjaminborbe.distributed.index.dao.DistributedIndexEntryDao;
import de.benjaminborbe.distributed.index.dao.DistributedIndexWordDao;
import de.benjaminborbe.storage.api.StorageException;

@Singleton
public class DistributedIndexServiceImpl implements DistributedIndexService {

	private final Logger logger;

	private final DistributedIndexEntryDao distributedIndexDao;

	private final DistributedIndexWordDao distributedIndexWordDao;

	@Inject
	public DistributedIndexServiceImpl(final Logger logger, final DistributedIndexEntryDao distributedIndexDao, final DistributedIndexWordDao distributedIndexWordDao) {
		this.logger = logger;
		this.distributedIndexDao = distributedIndexDao;
		this.distributedIndexWordDao = distributedIndexWordDao;
	}

	@Override
	public void add(final DistributedIndexIdentifier id, final Map<String, Integer> data) throws DistributedIndexServiceException {
		try {
			logger.debug("add - id: " + id);
			final DistributedIndexEntryBean bean = distributedIndexDao.create();
			bean.setId(id);
			bean.setData(data);
			distributedIndexDao.save(bean);
		}
		catch (final StorageException e) {
			throw new DistributedIndexServiceException(e);
		}
	}

	@Override
	public void remove(final DistributedIndexIdentifier id) throws DistributedIndexServiceException {
		try {
			logger.debug("remove - id: " + id);
			distributedIndexDao.remove(id);
		}
		catch (final StorageException e) {
			throw new DistributedIndexServiceException(e);
		}
	}

	@Override
	public DistributedIndexSearchResultIterator search(final Collection<String> words) throws DistributedIndexServiceException {
		try {
			logger.debug("search - words: " + StringUtils.join(words, ','));
			// TODO bborbe
			return distributedIndexWordDao.search(words.iterator().next());
		}
		catch (final StorageException e) {
			throw new DistributedIndexServiceException(e);
		}
	}

}
