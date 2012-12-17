package de.benjaminborbe.messageservice.dao;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.api.StorageService;
import de.benjaminborbe.storage.tools.DaoStorage;
import de.benjaminborbe.storage.tools.EntityIterator;
import de.benjaminborbe.storage.tools.IdentifierIterator;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.map.MapChain;

@Singleton
public class MessageDaoStorage extends DaoStorage<MessageBean, MessageIdentifier> implements MessageDao {

	@Inject
	public MessageDaoStorage(
			final Logger logger,
			final StorageService storageService,
			final Provider<MessageBean> beanProvider,
			final MessageBeanMapper mapper,
			final MessageIdentifierBuilder identifierBuilder,
			final CalendarUtil calendarUtil) {
		super(logger, storageService, beanProvider, mapper, identifierBuilder, calendarUtil);
	}

	private static final String COLUMN_FAMILY = "message_queue";

	@Override
	protected String getColumnFamily() {
		return COLUMN_FAMILY;
	}

	@Override
	public EntityIterator<MessageBean> getEntityIteratorForUser(final String type) throws StorageException {
		return getEntityIterator(new MapChain<String, String>().add(MessageBeanMapper.TYPE, type));
	}

	@Override
	public IdentifierIterator<MessageIdentifier> getIdentifierIteratorForUser(final String type) throws StorageException {
		return getIdentifierIterator(new MapChain<String, String>().add(MessageBeanMapper.TYPE, type));
	}
}
