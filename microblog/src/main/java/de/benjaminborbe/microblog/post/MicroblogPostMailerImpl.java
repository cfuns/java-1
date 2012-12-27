package de.benjaminborbe.microblog.post;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.mail.api.Mail;
import de.benjaminborbe.mail.api.MailServiceException;
import de.benjaminborbe.mail.api.MailService;
import de.benjaminborbe.microblog.api.MicroblogPostIdentifier;
import de.benjaminborbe.microblog.connector.MicroblogConnector;
import de.benjaminborbe.microblog.connector.MicroblogConnectorException;
import de.benjaminborbe.tools.util.StringUtil;

@Singleton
public class MicroblogPostMailerImpl implements MicroblogPostMailer {

	private static final int SUBJECT_MAX_LENGTH = 80;

	private final Logger logger;

	private final MailService mailService;

	private final MicroblogConnector microblogConnector;

	private final StringUtil stringUtil;

	@Inject
	public MicroblogPostMailerImpl(final Logger logger, final MailService mailService, final MicroblogConnector microblogConnector, final StringUtil stringUtil) {
		this.logger = logger;
		this.mailService = mailService;
		this.microblogConnector = microblogConnector;
		this.stringUtil = stringUtil;
	}

	@Override
	public void mailPost(final MicroblogPostIdentifier rev) throws MicroblogPostMailerException {
		logger.trace("send rev = " + rev);
		try {
			final Mail mail = buildMail(rev);
			mailService.send(mail);
		}
		catch (final MicroblogConnectorException e) {
			logger.error("MicroblogConnectorException", e);
			throw new MicroblogPostMailerException("MicroblogConnectorException", e);
		}
		catch (final MailServiceException e) {
			logger.error("MailSendException", e);
			throw new MicroblogPostMailerException("MailSendException", e);
		}
	}

	protected Mail buildMail(final MicroblogPostIdentifier revision) throws MicroblogConnectorException {
		final MicroblogPostResult post = microblogConnector.getPost(revision);
		final StringBuffer mailContent = new StringBuffer();
		mailContent.append(post.getContent());
		mailContent.append("\n\n");
		mailContent.append(post.getConversationUrl() != null ? post.getConversationUrl() : post.getPostUrl());
		final String from = post.getAuthor() + "@seibert-media.net";
		final String to = "bborbe@seibert-media.net";
		final String subject = "Micro: " + stringUtil.shorten(post.getContent(), SUBJECT_MAX_LENGTH);
		return new Mail(from, to, subject, mailContent.toString(), "text/plain");
	}

}
