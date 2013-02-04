package de.benjaminborbe.message.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

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
import de.benjaminborbe.message.util.MessageUnlock;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.tools.EntityIterator;
import de.benjaminborbe.storage.tools.EntityIteratorException;
import de.benjaminborbe.storage.tools.IdentifierIterator;
import de.benjaminborbe.storage.tools.IdentifierIteratorException;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.util.IdGeneratorUUID;
import de.benjaminborbe.tools.validation.ValidationExecutor;

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

	private final Provider<MessageConsumerExchanger> messageConsumerExchangerProvider;

	@Inject
	public MessageServiceImpl(
			final Logger logger,
			final Provider<MessageConsumerExchanger> messageConsumerExchangerProvider,
			final ValidationExecutor validationExecutor,
			final AnalyticsService analyticsService,
			final MessageDao messageDao,
			final IdGeneratorUUID idGeneratorUUID,
			final MessageUnlock messageUnlock,
			final AuthorizationService authorizationService,
			final CalendarUtil calendarUtil) {
		this.logger = logger;
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
		try {
			final MessageIdentifier messageIdentifier = new MessageIdentifier(type + "_" + id);
			if (messageDao.exists(messageIdentifier)) {
				logger.debug("message already exists => skip");
				return;
			}
			else {
				logger.debug("message not exists => sendMessage");
				final MessageBean bean = messageDao.create();
				bean.setId(messageIdentifier);
				bean.setType(type);
				bean.setContent(content);
				bean.setRetryCounter(0l);
				bean.setMaxRetryCounter(MessageConstants.MAX_RETRY);
				bean.setStartTime(calendarUtil.now());

				final ValidationResult errors = validationExecutor.validate(bean);
				if (errors.hasErrors()) {
					logger.warn(bean.getClass().getSimpleName() + " " + errors.toString());
					throw new ValidationException(errors);
				}
				validationExecutor.validate(bean);

				messageDao.save(bean);
				track(analyticsReportIdentifierMessageInsert);
			}
		}
		catch (final StorageException e) {
			throw new MessageServiceException(e);
		}
		catch (final ValidationException e) {
			throw new MessageServiceException(e);
		}
	}

	private void track(final AnalyticsReportIdentifier id) {
		try {
			analyticsService.addReportValue(id);
		}
		catch (final Exception e) {
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
			return messageUnlock.execute();
		}
		catch (final AuthorizationServiceException e) {
			throw new MessageServiceException(e);
		}
	}

	@Override
	public void deleteByType(final SessionIdentifier sessionIdentifier, final String type) throws MessageServiceException, PermissionDeniedException, LoginRequiredException {
		try {
			authorizationService.expectAdminRole(sessionIdentifier);

			final IdentifierIterator<MessageIdentifier> i = messageDao.getIdentifierIteratorForUser(type);
			while (i.hasNext()) {
				messageDao.delete(i.next());
			}
		}
		catch (final AuthorizationServiceException e) {
			throw new MessageServiceException(e);
		}
		catch (final StorageException e) {
			throw new MessageServiceException(e);
		}
		catch (final IdentifierIteratorException e) {
			throw new MessageServiceException(e);
		}
	}

	@Override
	public Collection<Message> getMessages(final SessionIdentifier sessionIdentifier) throws MessageServiceException, PermissionDeniedException, LoginRequiredException {
		try {
			authorizationService.expectAdminRole(sessionIdentifier);

			final List<Message> result = new ArrayList<Message>();
			final EntityIterator<MessageBean> i = messageDao.getEntityIterator();
			while (i.hasNext()) {
				result.add(i.next());
			}

			return result;
		}
		catch (final AuthorizationServiceException e) {
			throw new MessageServiceException(e);
		}
		catch (final StorageException e) {
			throw new MessageServiceException(e);
		}
		catch (final EntityIteratorException e) {
			throw new MessageServiceException(e);
		}
	}

	@Override
	public void deleteById(final SessionIdentifier sessionIdentifier, final MessageIdentifier messageIdentifier) throws MessageServiceException, PermissionDeniedException,
			LoginRequiredException {
		try {
			authorizationService.expectAdminRole(sessionIdentifier);

			messageDao.delete(messageIdentifier);
		}
		catch (final AuthorizationServiceException e) {
			throw new MessageServiceException(e);
		}
		catch (final StorageException e) {
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

			messageConsumerExchangerProvider.get().exchange();
			return true;
		}
		catch (final AuthorizationServiceException e) {
			throw new MessageServiceException(e);
		}
	}
}
