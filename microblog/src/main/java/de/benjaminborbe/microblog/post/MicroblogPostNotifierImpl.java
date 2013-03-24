package de.benjaminborbe.microblog.post;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.mail.api.MailDto;
import de.benjaminborbe.mail.api.MailService;
import de.benjaminborbe.mail.api.MailServiceException;
import de.benjaminborbe.microblog.api.MicroblogPost;
import de.benjaminborbe.microblog.connector.MicroblogConnectorException;
import de.benjaminborbe.tools.util.StringUtil;

@Singleton
public class MicroblogPostNotifierImpl implements MicroblogPostNotifier {

	private static final int SUBJECT_MAX_LENGTH = 80;

	private final Logger logger;

	private final MailService mailService;

	private final StringUtil stringUtil;

	@Inject
	public MicroblogPostNotifierImpl(final Logger logger, final MailService mailService, final StringUtil stringUtil) {
		this.logger = logger;
		this.mailService = mailService;
		this.stringUtil = stringUtil;
	}

	@Override
	public void mailPost(final MicroblogPost rev) throws MicroblogPostNotifierException {
		try {
			logger.trace("send rev = " + rev);
			final MailDto mail = buildMail(rev);
			mailService.send(mail);
		}
		catch (final MailServiceException | MicroblogConnectorException e) {
			throw new MicroblogPostNotifierException("MailSendException", e);
		}
	}

	protected MailDto buildMail(final MicroblogPost post) throws MicroblogConnectorException {
		final StringBuffer mailContent = new StringBuffer();
		mailContent.append(post.getContent());
		mailContent.append("\n\n");
		mailContent.append(post.getConversationUrl() != null ? post.getConversationUrl() : post.getPostUrl());
		final String from = post.getAuthor() + "@seibert-media.net";
		final String to = "bborbe@seibert-media.net";
		final String subject = "Micro: " + stringUtil.shorten(post.getContent(), SUBJECT_MAX_LENGTH);
		return new MailDto(from, to, subject, mailContent.toString(), "text/plain");
	}

}
