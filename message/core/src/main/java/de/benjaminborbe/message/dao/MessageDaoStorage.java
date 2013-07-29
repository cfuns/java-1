package de.benjaminborbe.message.dao;

import com.google.common.base.Predicate;
import com.google.inject.Provider;
import de.benjaminborbe.message.api.MessageIdentifier;
import de.benjaminborbe.message.util.MessageUtil;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.api.StorageService;
import de.benjaminborbe.storage.tools.DaoStorage;
import de.benjaminborbe.storage.tools.EntityIterator;
import de.benjaminborbe.storage.tools.EntityIteratorFilter;
import de.benjaminborbe.storage.tools.IdentifierIterator;
import de.benjaminborbe.storage.tools.StorageValueMap;
import de.benjaminborbe.tools.date.CalendarUtil;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class MessageDaoStorage extends DaoStorage<MessageBean, MessageIdentifier> implements MessageDao {

	private final class MessageLockExpiredPredicate implements Predicate<MessageBean> {

		@Override
		public boolean apply(final MessageBean bean) {
			return bean != null && messageUtil.isMessageLockExpired(bean);
		}
	}

	private final MessageUtil messageUtil;

	@Inject
	public MessageDaoStorage(
		final Logger logger,
		final MessageUtil messageUtil,
		final StorageService storageService,
		final Provider<MessageBean> beanProvider,
		final MessageBeanMapper mapper,
		final MessageIdentifierBuilder identifierBuilder,
		final CalendarUtil calendarUtil
	) {
		super(logger, storageService, beanProvider, mapper, identifierBuilder, calendarUtil);
		this.messageUtil = messageUtil;
	}

	private static final String COLUMN_FAMILY = "message_queue";

	@Override
	protected String getColumnFamily() {
		return COLUMN_FAMILY;
	}

	@Override
	public EntityIterator<MessageBean> getEntityIteratorForType(final String type) throws StorageException {
		return getEntityIterator(new StorageValueMap(getEncoding()).add(MessageBeanMapper.TYPE, type));
	}

	@Override
	public IdentifierIterator<MessageIdentifier> getIdentifierIteratorForUser(final String type) throws StorageException {
		return getIdentifierIterator(new StorageValueMap(getEncoding()).add(MessageBeanMapper.TYPE, type));
	}

	@Override
	public EntityIterator<MessageBean> findExpired() throws StorageException {
		return new EntityIteratorFilter<MessageBean>(getEntityIterator(), new MessageLockExpiredPredicate());
	}
}
