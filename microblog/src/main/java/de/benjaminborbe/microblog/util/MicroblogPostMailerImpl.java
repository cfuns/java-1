package de.benjaminborbe.microblog.util;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.mail.api.Mail;
import de.benjaminborbe.mail.api.MailSendException;
import de.benjaminborbe.mail.api.MailService;

@Singleton
public class MicroblogPostMailerImpl implements MicroblogPostMailer {

	private final Logger logger;

	private final MailService mailService;

	private final MicroblogConnector microblogConnector;

	@Inject
	public MicroblogPostMailerImpl(final Logger logger, final MailService mailService, final MicroblogConnector microblogConnector) {
		this.logger = logger;
		this.mailService = mailService;
		this.microblogConnector = microblogConnector;
	}

	@Override
	public void mailPost(final long rev) throws MicroblogPostMailerException {
		logger.trace("send rev = " + rev);
		try {
			final Mail mail = buildMail(rev);
			mailService.send(mail);
		}
		catch (final MicroblogConnectorException e) {
			logger.error("MicroblogConnectorException", e);
			throw new MicroblogPostMailerException("MicroblogConnectorException", e);
		}
		catch (final MailSendException e) {
			logger.error("MailSendException", e);
			throw new MicroblogPostMailerException("MailSendException", e);
		}
	}

	protected Mail buildMail(final long revision) throws MicroblogConnectorException {
		final MicroblogPostResult post = microblogConnector.getPost(revision);
		final StringBuffer mailContent = new StringBuffer();
		mailContent.append(post.getContent());
		mailContent.append("\n\n");
		mailContent.append(post.getConversationUrl() != null ? post.getConversationUrl() : post.getPostUrl());
		final String from = post.getAuthor() + "@seibert-media.net";
		final String to = "bborbe@seibert-media.net";
		final String subject = "Micro: " + preview(post.getContent());
		return new Mail(from, to, subject, mailContent.toString(), "text/plain");
	}

	protected String preview(final String content) {
		if (content != null && content.length() > 80) {
			return content.substring(0, 80);
		}
		return content;
	}
}
