package de.benjaminborbe.message.service;

import com.google.inject.Provider;
import de.benjaminborbe.analytics.api.AnalyticsReportIdentifier;
import de.benjaminborbe.analytics.api.AnalyticsService;
import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.api.ValidationResult;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.AuthorizationServiceException;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.message.MessageConstants;
import de.benjaminborbe.message.api.Message;
import de.benjaminborbe.message.api.MessageIdentifier;
import de.benjaminborbe.message.api.MessageService;
import de.benjaminborbe.message.api.MessageServiceException;
import de.benjaminborbe.message.dao.MessageBean;
import de.benjaminborbe.message.dao.MessageDao;
import de.benjaminborbe.message.util.MessageConsumerExchanger;
import de.benjaminborbe.message.util.MessageLock;
import de.benjaminborbe.message.util.MessageUnlock;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.tools.EntityIterator;
import de.benjaminborbe.storage.tools.EntityIteratorException;
import de.benjaminborbe.storage.tools.IdentifierIterator;
import de.benjaminborbe.storage.tools.IdentifierIteratorException;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.util.IdGeneratorUUID;
import de.benjaminborbe.tools.validation.ValidationExecutor;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

@Singleton
public class MessageServiceImpl implements MessageService {

	private final Logger logger;

	private final MessageDao messageDao;

	private final IdGeneratorUUID idGeneratorUUID;

	private final MessageUnlock messageUnlock;

	private final AuthorizationService authorizationService;

	private final AnalyticsService analyticsService;

	private final AnalyticsReportIdentifier analyticsReportIdentifierMessageInsert = new AnalyticsReportIdentifier("MessageInsert");

	private final CalendarUtil calendarUtil;

	private final ValidationExecutor validationExecutor;

	private final MessageLock messageLock;

	private final Provider<MessageConsumerExchanger> messageConsumerExchangerProvider;

	@Inject
	public MessageServiceImpl(
		final Logger logger,
		final MessageLock messageLock,
		final Provider<MessageConsumerExchanger> messageConsumerExchangerProvider,
		final ValidationExecutor validationExecutor,
		final AnalyticsService analyticsService,
		final MessageDao messageDao,
		final IdGeneratorUUID idGeneratorUUID,
		final MessageUnlock messageUnlock,
		final AuthorizationService authorizationService,
		final CalendarUtil calendarUtil
	) {
		this.logger = logger;
		this.messageLock = messageLock;
		this.messageConsumerExchangerProvider = messageConsumerExchangerProvider;
		this.validationExecutor = validationExecutor;
		this.analyticsService = analyticsService;
		this.messageDao = messageDao;
		this.idGeneratorUUID = idGeneratorUUID;
		this.messageUnlock = messageUnlock;
		this.authorizationService = authorizationService;
		this.calendarUtil = calendarUtil;
	}

	@Override
	public void sendMessage(final String type, final String id, final String content) throws MessageServiceException {
		sendMessage(type, id, content, calendarUtil.now());
	}

	@Override
	public void sendMessage(final String type, final String id, final String content, final Calendar startTime) throws MessageServiceException {
		try {
			logger.debug("sendMessage with type: " + type + " and id: " + id);

			final MessageIdentifier messageIdentifier = new MessageIdentifier(type + "_" + id);
			if (messageDao.exists(messageIdentifier)) {
				logger.trace("message already exists => skip");
				return;
			} else {
				logger.trace("message not exists => sendMessage");
				final MessageBean bean = messageDao.create();
				bean.setId(messageIdentifier);
				bean.setType(type);
				bean.setContent(content);
				bean.setRetryCounter(0l);
				bean.setMaxRetryCounter(MessageConstants.MAX_RETRY);
				bean.setStartTime(startTime);

				final ValidationResult errors = validationExecutor.validate(bean);
				if (errors.hasErrors()) {
					logger.warn(bean.getClass().getSimpleName() + " " + errors.toString());
					throw new ValidationException(errors);
				}
				validationExecutor.validate(bean);

				messageDao.save(bean);
				track(analyticsReportIdentifierMessageInsert);
			}
		} catch (final StorageException | ValidationException e) {
			throw new MessageServiceException(e);
		}
	}

	private void track(final AnalyticsReportIdentifier id) {
		try {
			analyticsService.addReportValue(id);
		} catch (final Exception e) {
			logger.warn("track " + id + " failed", e);
		}
	}

	@Override
	public void sendMessage(final String type, final String content) throws MessageServiceException {
		sendMessage(type, idGeneratorUUID.nextId(), content);
	}

	@Override
	public boolean unlockExpiredMessages(final SessionIdentifier sessionIdentifier) throws PermissionDeniedException, LoginRequiredException, MessageServiceException {
		try {
			authorizationService.expectAdminRole(sessionIdentifier);

			logger.debug("unlock expired messages");

			return messageUnlock.execute();
		} catch (final AuthorizationServiceException e) {
			throw new MessageServiceException(e);
		}
	}

	@Override
	public String getLockName(final SessionIdentifier sessionIdentifier) throws MessageServiceException, LoginRequiredException, PermissionDeniedException {
		try {
			authorizationService.expectAdminRole(sessionIdentifier);

			logger.debug("get lockName");

			return messageLock.getLockName();
		} catch (final AuthorizationServiceException e) {
			throw new MessageServiceException(e);
		}
	}

	@Override
	public void deleteByType(
		final SessionIdentifier sessionIdentifier,
		final String type
	) throws MessageServiceException, PermissionDeniedException, LoginRequiredException {
		try {
			authorizationService.expectAdminRole(sessionIdentifier);

			logger.debug("delete message with type: " + type);

			final IdentifierIterator<MessageIdentifier> i = messageDao.getIdentifierIteratorForUser(type);
			while (i.hasNext()) {
				final MessageIdentifier messageIdentifier = i.next();
				messageDao.delete(messageIdentifier);
			}
		} catch (final AuthorizationServiceException | IdentifierIteratorException | StorageException e) {
			throw new MessageServiceException(e);
		}
	}

	@Override
	public Collection<Message> getMessages(final SessionIdentifier sessionIdentifier) throws MessageServiceException, PermissionDeniedException, LoginRequiredException {
		try {
			authorizationService.expectAdminRole(sessionIdentifier);

			logger.debug("getMessages");

			final List<Message> result = new ArrayList<>();
			final EntityIterator<MessageBean> i = messageDao.getEntityIterator();
			while (i.hasNext()) {
				result.add(i.next());
			}

			return result;
		} catch (final AuthorizationServiceException | EntityIteratorException | StorageException e) {
			throw new MessageServiceException(e);
		}
	}

	@Override
	public Message getMessage(
		final SessionIdentifier sessionIdentifier, final MessageIdentifier messageIdentifier
	) throws MessageServiceException, PermissionDeniedException, LoginRequiredException {
		try {
			authorizationService.expectAdminRole(sessionIdentifier);
			logger.debug("get message with id: " + messageIdentifier);
			return messageDao.load(messageIdentifier);
		} catch (final AuthorizationServiceException | StorageException e) {
			throw new MessageServiceException(e);
		}
	}

	@Override
	public void deleteById(
		final SessionIdentifier sessionIdentifier,
		final MessageIdentifier messageIdentifier
	) throws MessageServiceException, PermissionDeniedException,
		LoginRequiredException {
		try {
			authorizationService.expectAdminRole(sessionIdentifier);

			logger.debug("deleteById - message: " + messageIdentifier);

			messageDao.delete(messageIdentifier);
		} catch (final AuthorizationServiceException | StorageException e) {
			throw new MessageServiceException(e);
		}
	}

	@Override
	public MessageIdentifier createMessageIdentifier(final String id) throws MessageServiceException {
		return id != null ? new MessageIdentifier(id) : null;
	}

	@Override
	public boolean exchangeMessages(final SessionIdentifier sessionIdentifier) throws MessageServiceException, LoginRequiredException, PermissionDeniedException {
		try {
			authorizationService.expectAdminRole(sessionIdentifier);
			logger.debug("exchangeMessages");

			messageConsumerExchangerProvider.get().exchange();
			return true;
		} catch (final AuthorizationServiceException e) {
			throw new MessageServiceException(e);
		}
	}
}
