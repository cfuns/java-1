package de.benjaminborbe.distributed.index.dao;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.distributed.index.api.DistributedIndexIdentifier;

@Singleton
public class DistributedIndexDaoStorage implements DistributedIndexDao {

	private final Provider<DistributedIndexBean> provider;

	@Inject
	public DistributedIndexDaoStorage(final Provider<DistributedIndexBean> provider) {
		this.provider = provider;
	}

	@Override
	public DistributedIndexBean load(final DistributedIndexIdentifier id) {
		return null;
	}

	@Override
	public void remove(final DistributedIndexIdentifier id) {
	}

	@Override
	public void save(final DistributedIndexBean bean) {
	}

	@Override
	public DistributedIndexBean create() {
		return provider.get();
	}
}
