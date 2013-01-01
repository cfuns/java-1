package de.benjaminborbe.distributed.index.service;

import java.util.Collection;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.distributed.index.api.DistributedIndexSearchResultIterator;
import de.benjaminborbe.distributed.index.api.DistributedIndexService;
import de.benjaminborbe.distributed.index.api.DistributedIndexServiceException;
import de.benjaminborbe.distributed.index.dao.DistributedIndexEntryBean;
import de.benjaminborbe.distributed.index.dao.DistributedIndexEntryDao;
import de.benjaminborbe.distributed.index.dao.DistributedIndexEntryIdentifier;
import de.benjaminborbe.distributed.index.dao.DistributedIndexWordDao;
import de.benjaminborbe.distributed.index.dao.DistributedIndexWordIdentifier;
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
	public void add(final String index, final String id, final Map<String, Integer> data) throws DistributedIndexServiceException {
		try {
			logger.debug("add - index: " + index + " id: " + id);
			final DistributedIndexEntryBean bean = distributedIndexDao.create();
			bean.setId(buildId(index, id));
			bean.setData(data);
			distributedIndexDao.save(bean);
		}
		catch (final StorageException e) {
			throw new DistributedIndexServiceException(e);
		}
	}

	@Override
	public void remove(final String index, final String id) throws DistributedIndexServiceException {
		try {
			logger.debug("remove - index: " + index + " id: " + id);
			distributedIndexDao.remove(buildId(index, id));
		}
		catch (final StorageException e) {
			throw new DistributedIndexServiceException(e);
		}
	}

	private DistributedIndexEntryIdentifier buildId(final String index, final String id) {
		return new DistributedIndexEntryIdentifier(index, id);
	}

	@Override
	public DistributedIndexSearchResultIterator search(final String index, final Collection<String> words) throws DistributedIndexServiceException {
		try {
			logger.debug("search - index: " + index + " words: " + StringUtils.join(words, ','));
			return distributedIndexWordDao.search(buildWordId(index, words.iterator().next()));
		}
		catch (final StorageException e) {
			throw new DistributedIndexServiceException(e);
		}
	}

	private DistributedIndexWordIdentifier buildWordId(final String index, final String word) {
		return new DistributedIndexWordIdentifier(index, word);
	}

}
