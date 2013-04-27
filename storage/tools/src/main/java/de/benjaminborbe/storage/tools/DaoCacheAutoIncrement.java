package de.benjaminborbe.storage.tools;

import com.google.inject.Provider;
import de.benjaminborbe.api.Identifier;
import de.benjaminborbe.tools.util.IdGenerator;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public abstract class DaoCacheAutoIncrement<E extends Entity<I>, I extends Identifier<String>> extends DaoCache<E, I> {

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
