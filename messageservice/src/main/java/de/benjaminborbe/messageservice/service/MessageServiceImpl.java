package de.benjaminborbe.messageservice.service;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.messageservice.api.Message;
import de.benjaminborbe.messageservice.api.MessageService;
import de.benjaminborbe.messageservice.api.MessageServiceException;
import de.benjaminborbe.messageservice.dao.MessageBean;
import de.benjaminborbe.messageservice.dao.MessageDao;
import de.benjaminborbe.messageservice.dao.MessageIdentifier;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.tools.util.IdGeneratorUUID;

@Singleton
public class MessageServiceImpl implements MessageService {

	private final Logger logger;

	private final MessageDao messageDao;

	private final IdGeneratorUUID idGeneratorUUID;

	@Inject
	public MessageServiceImpl(final Logger logger, final MessageDao messageDao, final IdGeneratorUUID idGeneratorUUID) {
		this.logger = logger;
		this.messageDao = messageDao;
		this.idGeneratorUUID = idGeneratorUUID;
	}

	@Override
	public void sendMessage(final Message message) throws MessageServiceException {
		sendMessage(message.getType(), message.getContent());
	}

	@Override
	public void sendMessage(final String type, final String content) throws MessageServiceException {
		try {
			logger.debug("sendMessage");
			final MessageBean bean = messageDao.create();
			bean.setId(new MessageIdentifier(idGeneratorUUID.nextId()));
			bean.setType(type);
			bean.setContent(content);
			bean.setRetryCounter(0l);
			messageDao.save(bean);
		}
		catch (final StorageException e) {
			throw new MessageServiceException(e);
		}
	}
}
