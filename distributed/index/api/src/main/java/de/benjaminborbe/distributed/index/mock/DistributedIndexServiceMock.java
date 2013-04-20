package de.benjaminborbe.distributed.index.mock;

import de.benjaminborbe.distributed.index.api.DistributedIndexSearchResultIterator;
import de.benjaminborbe.distributed.index.api.DistributedIndexService;
import de.benjaminborbe.distributed.index.api.DistributedIndexServiceException;

import java.util.Collection;
import java.util.Map;

public class DistributedIndexServiceMock implements DistributedIndexService {

	public DistributedIndexServiceMock() {
	}

	@Override
	public void add(final String index, final String id, final Map<String, Integer> data) throws DistributedIndexServiceException {

	}

	@Override
	public void remove(final String index, final String id) throws DistributedIndexServiceException {
	}

	@Override
	public DistributedIndexSearchResultIterator search(final String index, final Collection<String> words) throws DistributedIndexServiceException {
		return null;
	}

	@Override
	public Map<String, Integer> getWordRatingForEntry(final String index, final String url) throws DistributedIndexServiceException {
		return null;
	}

	@Override
	public Map<String, Integer> getEntryRatingForWord(final String index, final String word) throws DistributedIndexServiceException {
		return null;
	}

}
