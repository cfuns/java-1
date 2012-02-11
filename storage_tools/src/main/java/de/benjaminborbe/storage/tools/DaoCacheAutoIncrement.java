package de.benjaminborbe.storage.tools;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.tools.util.IdGenerator;

@Singleton
public abstract class DaoCacheAutoIncrement<T extends Entity<Long>> extends DaoCache<T, Long> {

	private final IdGenerator<Long> idGenerator;

	@Inject
	public DaoCacheAutoIncrement(final Logger logger, final IdGenerator<Long> idGenerator, final Provider<T> provider) {
		super(logger, provider);
		this.idGenerator = idGenerator;
	}

	@Override
	public void save(final T entity) {
		logger.trace("save");
		if (entity.getId() == null) {
			entity.setId(idGenerator.nextId());
		}
		super.save(entity);
	}

}
