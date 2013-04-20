package de.benjaminborbe.distributed.index.service;

import javax.inject.Inject;
import javax.inject.Singleton;
import de.benjaminborbe.distributed.index.api.DistributedIndexSearchResult;
import de.benjaminborbe.distributed.index.api.DistributedIndexSearchResultIterator;
import de.benjaminborbe.distributed.index.api.DistributedIndexService;
import de.benjaminborbe.distributed.index.api.DistributedIndexServiceException;
import de.benjaminborbe.distributed.index.dao.DistributedIndexEntryBean;
import de.benjaminborbe.distributed.index.dao.DistributedIndexEntryDao;
import de.benjaminborbe.distributed.index.dao.DistributedIndexEntryIdentifier;
import de.benjaminborbe.distributed.index.dao.DistributedIndexWordDao;
import de.benjaminborbe.distributed.index.dao.DistributedIndexWordIdentifier;
import de.benjaminborbe.distributed.index.util.DistributedIndexSearchResultIteratorAgreator;
import de.benjaminborbe.distributed.index.util.DistributedIndexSearchResultIteratorCurrent;
import de.benjaminborbe.storage.api.StorageException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
			logger.debug("add - index: " + index + " id: " + id + " data: " + data);
			final DistributedIndexEntryBean bean = distributedIndexDao.create();
			bean.setId(buildId(index, id));
			bean.setData(data);
			distributedIndexDao.save(bean);
		} catch (final StorageException e) {
			throw new DistributedIndexServiceException(e);
		}
	}

	@Override
	public void remove(final String index, final String id) throws DistributedIndexServiceException {
		try {
			logger.debug("remove - index: " + index + " id: " + id);
			distributedIndexDao.remove(buildId(index, id));
		} catch (final StorageException e) {
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

			final List<DistributedIndexSearchResultIteratorCurrent> iterators = new ArrayList<>();
			for (final String word : words) {
				iterators.add(new DistributedIndexSearchResultIteratorCurrent(distributedIndexWordDao.search(buildWordId(index, word))));
			}
			return new DistributedIndexSearchResultIteratorAgreator(logger, iterators);
		} catch (final StorageException e) {
			throw new DistributedIndexServiceException(e);
		}
	}

	private DistributedIndexWordIdentifier buildWordId(final String index, final String word) {
		return new DistributedIndexWordIdentifier(index, word);
	}

	@Override
	public Map<String, Integer> getWordRatingForEntry(final String index, final String id) throws DistributedIndexServiceException {
		try {
			logger.debug("getWordRatingForEntry - index: " + index + " id: " + id);
			final DistributedIndexEntryBean bean = distributedIndexDao.load(buildId(index, id));
			return bean.getData();
		} catch (final StorageException e) {
			throw new DistributedIndexServiceException(e);
		}
	}

	@Override
	public Map<String, Integer> getEntryRatingForWord(final String index, final String word) throws DistributedIndexServiceException {
		try {
			logger.debug("getEntryRatingForWord - index: " + index + " word: " + word);
			final Map<String, Integer> data = new HashMap<>();
			final DistributedIndexSearchResultIteratorCurrent iterator = new DistributedIndexSearchResultIteratorCurrent(distributedIndexWordDao.search(buildWordId(index, word)));

			while (iterator.hasNext()) {
				final DistributedIndexSearchResult r = iterator.next();
				data.put(r.getId(), r.getRating());
			}
			return data;
		} catch (final StorageException e) {
			throw new DistributedIndexServiceException(e);
		}
	}

}
