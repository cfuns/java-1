package de.benjaminborbe.storage.tools;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.api.Identifier;
import de.benjaminborbe.tools.util.IdGenerator;

@Singleton
public abstract class DaoCacheAutoIncrement<E extends Entity<I>, I extends Identifier<?>> extends DaoCache<E, I> {

	private final IdGenerator<I> idGenerator;

	@Inject
	public DaoCacheAutoIncrement(final Logger logger, final IdGenerator<I> idGenerator, final Provider<E> provider) {
		super(logger, provider);
		this.idGenerator = idGenerator;
	}

	@Override
	public void save(final E entity) {
		logger.trace("save");
		if (entity.getId() == null) {
			final I id = idGenerator.nextId();
			entity.setId(id);
		}
		super.save(entity);
	}

}
