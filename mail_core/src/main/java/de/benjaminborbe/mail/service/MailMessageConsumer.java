package de.benjaminborbe.mail.service;

import org.slf4j.Logger;

import com.google.inject.Inject;

import de.benjaminborbe.mail.MailConstants;
import de.benjaminborbe.mail.api.Mail;
import de.benjaminborbe.mail.api.MailServiceException;
import de.benjaminborbe.mail.util.MailJsonMapper;
import de.benjaminborbe.mail.util.MailSender;
import de.benjaminborbe.message.api.Message;
import de.benjaminborbe.message.api.MessageConsumer;
import de.benjaminborbe.tools.mapper.MapException;

public class MailMessageConsumer implements MessageConsumer {

	private final MailJsonMapper mailJsonMapper;

	private final MailSender mailSender;

	private final Logger logger;

	@Inject
	public MailMessageConsumer(final Logger logger, final MailJsonMapper mailJsonMapper, final MailSender mailSender) {
		this.logger = logger;
		this.mailJsonMapper = mailJsonMapper;
		this.mailSender = mailSender;
	}

	@Override
	public String getType() {
		return MailConstants.MAIL_SEND_TYPE;
	}

	@Override
	public boolean process(final Message message) {
		try {
			logger.debug("process mail message");
			final Mail mail = mailJsonMapper.map(message.getContent());
			mailSender.send(mail);
			return true;
		}
		catch (final MailServiceException e) {
			logger.warn(e.getClass().getName(), e);
			return false;
		}
		catch (final MapException e) {
			logger.warn(e.getClass().getName(), e);
			return false;
		}
	}
}
