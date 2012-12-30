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

@Singleton
public class DistributedIndexServiceImpl implements DistributedIndexService {

	private final Logger logger;

	@Inject
	public DistributedIndexServiceImpl(final Logger logger) {
		this.logger = logger;
	}

	@Override
	public void add(final DistributedIndexIdentifier id, final Map<String, Integer> data) throws DistributedIndexServiceException {
		logger.debug("add - id: " + id);
	}

	@Override
	public void remove(final DistributedIndexIdentifier id) throws DistributedIndexServiceException {
		logger.debug("remove - id: " + id);
	}

	@Override
	public DistributedIndexSearchResultIterator search(final Collection<String> words) throws DistributedIndexServiceException {
		logger.debug("search - words: " + StringUtils.join(words, ','));
		return null;
	}

}
