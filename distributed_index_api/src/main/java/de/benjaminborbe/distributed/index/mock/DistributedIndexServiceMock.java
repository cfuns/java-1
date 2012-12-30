package de.benjaminborbe.distributed.index.mock;

import java.util.Collection;
import java.util.Map;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.distributed.index.api.DistributedIndexIdentifier;
import de.benjaminborbe.distributed.index.api.DistributedIndexSearchResultIterator;
import de.benjaminborbe.distributed.index.api.DistributedIndexService;
import de.benjaminborbe.distributed.index.api.DistributedIndexServiceException;

@Singleton
public class DistributedIndexServiceMock implements DistributedIndexService {

	@Inject
	public DistributedIndexServiceMock() {
	}

	@Override
	public void add(final DistributedIndexIdentifier id, final Map<String, Integer> data) throws DistributedIndexServiceException {
	}

	@Override
	public void remove(final DistributedIndexIdentifier id) throws DistributedIndexServiceException {
	}

	@Override
	public DistributedIndexSearchResultIterator search(final Collection<String> words) throws DistributedIndexServiceException {
		return null;
	}

}
