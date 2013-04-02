package de.benjaminborbe.distributed.index.util;

import de.benjaminborbe.distributed.index.api.DistributedIndexSearchResult;
import de.benjaminborbe.distributed.index.api.DistributedIndexSearchResultIterator;
import de.benjaminborbe.distributed.index.api.DistributedIndexServiceException;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

public class DistributedIndexSearchResultIteratorAgreator implements DistributedIndexSearchResultIterator {

	private final List<DistributedIndexSearchResultIteratorCurrent> iterators;

	private final List<Map<String, Integer>> cache = new ArrayList<>();

	private DistributedIndexSearchResult next;

	private final Logger logger;

	public DistributedIndexSearchResultIteratorAgreator(final Logger logger, final List<DistributedIndexSearchResultIteratorCurrent> iterators) {
		this.logger = logger;
		this.iterators = iterators;
		for (int i = 0; i < iterators.size(); ++i) {
			cache.add(new HashMap<String, Integer>());
		}
	}

	@Override
	public DistributedIndexSearchResult next() throws DistributedIndexServiceException {
		logger.trace("next");
		if (hasNext()) {
			final DistributedIndexSearchResult result = next;
			next = null;
			return result;
		} else {
			throw new NoSuchElementException();
		}
	}

	@Override
	public boolean hasNext() throws DistributedIndexServiceException {
		logger.trace("next");
		while (true) {
			if (next != null) {
				logger.trace("next => true");
				return true;
			}

			DistributedIndexSearchResult highestResult = null;
			Integer highestPos = null;
			for (int i = 0; i < iterators.size(); ++i) {
				final DistributedIndexSearchResultIteratorCurrent iterator = iterators.get(i);
				if (iterator.getCurrent() == null && iterator.hasNext()) {
					iterator.next();
				}
				if (iterator.getCurrent() != null) {
					if (highestResult == null || highestResult.getRating() < iterator.getCurrent().getRating()) {
						highestResult = iterator.getCurrent();
						highestPos = i;
					}
				}
			}
			if (highestPos == null) {
				return false;
			}
			iterators.get(highestPos).setCurrent(null);
			cache.get(highestPos).put(highestResult.getId(), highestResult.getRating());
			next = buildNext(highestResult);
		}
	}

	private DistributedIndexSearchResult buildNext(final DistributedIndexSearchResult highestResult) {
		int rating = 0;
		for (final Map<String, Integer> c : cache) {
			final Integer r = c.get(highestResult.getId());
			if (r != null) {
				rating += r;
			} else {
				return null;
			}
		}
		return new DistributedIndexSearchResultImpl(rating, highestResult.getIndex(), highestResult.getId());
	}
}
