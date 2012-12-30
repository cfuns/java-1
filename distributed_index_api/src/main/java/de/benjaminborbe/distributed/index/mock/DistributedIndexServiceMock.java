package de.benjaminborbe.distributed.index.mock;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.distributed.index.api.DistributedIndexService;

@Singleton
public class DistributedIndexServiceMock implements DistributedIndexService {

	@Inject
	public DistributedIndexServiceMock() {
	}

	@Override
	public void execute() {
	}
}
