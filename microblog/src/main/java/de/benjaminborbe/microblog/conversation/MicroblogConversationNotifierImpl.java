package de.benjaminborbe.microblog.conversation;

import java.util.List;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.mail.api.MailDto;
import de.benjaminborbe.mail.api.MailService;
import de.benjaminborbe.mail.api.MailServiceException;
import de.benjaminborbe.microblog.api.MicroblogConversationIdentifier;
import de.benjaminborbe.microblog.connector.MicroblogConnector;
import de.benjaminborbe.microblog.connector.MicroblogConnectorException;
import de.benjaminborbe.microblog.post.MicroblogPostResult;
import de.benjaminborbe.tools.util.StringUtil;

@Singleton
public class MicroblogConversationNotifierImpl implements MicroblogConversationNotifier {

	private static final int SUBJECT_MAX_LENGTH = 80;

	private final Logger logger;

	private final MailService mailService;

	private final MicroblogConnector microblogConnector;

	private final StringUtil stringUtil;

	@Inject
	public MicroblogConversationNotifierImpl(final Logger logger, final MicroblogConnector microblogConnector, final MailService mailService, final StringUtil stringUtil) {
		this.logger = logger;
		this.microblogConnector = microblogConnector;
		this.mailService = mailService;
		this.stringUtil = stringUtil;
	}

	@Override
	public void mailConversation(final MicroblogConversationIdentifier microblogConversationIdentifier) throws MicroblogConversationNotifierException {
		logger.trace("mailConversation with rev " + microblogConversationIdentifier);
		try {
			final MailDto mail = buildMail(microblogConversationIdentifier);
			mailService.send(mail);
		}
		catch (final MicroblogConnectorException e) {
			logger.error("MicroblogConnectorException", e);
			throw new MicroblogConversationNotifierException(e.getClass().getSimpleName(), e);
		}
		catch (final MailServiceException e) {
			logger.error("MailSendException", e);
			throw new MicroblogConversationNotifierException(e.getClass().getSimpleName(), e);
		}
	}

	private MailDto buildMail(final MicroblogConversationIdentifier microblogConversationIdentifier) throws MicroblogConnectorException {
		final MicroblogConversationResult microblogConversationResult = microblogConnector.getConversation(microblogConversationIdentifier);

		final List<MicroblogPostResult> posts = microblogConversationResult.getPosts();
		if (posts == null || posts.size() == 0) {
			throw new MicroblogConnectorException("no posts found");
		}
		final MicroblogPostResult firstPost = posts.get(0);
		final MicroblogPostResult lastPost = posts.get(posts.size() - 1);
		final StringBuffer mailContent = new StringBuffer();
		for (final MicroblogPostResult post : posts) {
			mailContent.append(post.getAuthor() + ": " + post.getContent());
			mailContent.append("\n\n");
		}
		mailContent.append(microblogConversationResult.getConversationUrl());
		final String from = lastPost.getAuthor() + "@seibert-media.net";
		final String to = "bborbe@seibert-media.net";
		final String subject = "Micro: " + stringUtil.shorten(firstPost.getContent(), SUBJECT_MAX_LENGTH);
		return new MailDto(from, to, subject, mailContent.toString(), "text/plain");
	}

}
