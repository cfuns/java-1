package de.benjaminborbe.mail.service;

import de.benjaminborbe.mail.MailConstants;
import de.benjaminborbe.mail.api.Mail;
import de.benjaminborbe.mail.api.MailService;
import de.benjaminborbe.mail.api.MailServiceException;
import de.benjaminborbe.mail.util.MailJsonMapper;
import de.benjaminborbe.message.api.MessageService;
import de.benjaminborbe.message.api.MessageServiceException;
import de.benjaminborbe.tools.mapper.MapException;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class MailServiceImpl implements MailService {

	private final Logger logger;

	private final MessageService messageService;

	private final MailJsonMapper mailJsonMapper;

	@Inject
	public MailServiceImpl(final Logger logger, final MessageService messageService, final MailJsonMapper mailJsonMapper) {
		this.logger = logger;
		this.messageService = messageService;
		this.mailJsonMapper = mailJsonMapper;
	}

	@Override
	public void send(final Mail mail) throws MailServiceException {
		try {
			logger.debug("send mail from: " + mail.getFrom() + " to: " + mail.getTo() + " subject: " + mail.getSubject());
			messageService.sendMessage(MailConstants.MAIL_SEND_TYPE, mailJsonMapper.map(mail));
		} catch (final MapException e) {
			throw new MailServiceException(e);
		} catch (final MessageServiceException e) {
			throw new MailServiceException(e);
		}
	}

}
