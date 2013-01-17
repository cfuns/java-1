package de.benjaminborbe.lunch.service;

import java.util.Arrays;
import java.util.Collection;

import org.slf4j.Logger;

import com.google.inject.Inject;

import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.lunch.dao.LunchUserSettingsDao;
import de.benjaminborbe.microblog.api.MicroblogPost;
import de.benjaminborbe.microblog.api.MicroblogPostListener;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.tools.IdentifierIterator;
import de.benjaminborbe.storage.tools.IdentifierIteratorException;
import de.benjaminborbe.xmpp.api.XmppService;
import de.benjaminborbe.xmpp.api.XmppServiceException;

public class LunchMicroblogPostListener implements MicroblogPostListener {

	private final Collection<String> words = Arrays.asList("Essen", "Mittagessen", "Mittagstisch");

	private final Logger logger;

	private final XmppService xmppService;

	private final LunchUserSettingsDao lunchUserSettingsDao;

	@Inject
	public LunchMicroblogPostListener(final Logger logger, final XmppService xmppService, final LunchUserSettingsDao lunchUserSettingsDao) {
		this.logger = logger;
		this.xmppService = xmppService;
		this.lunchUserSettingsDao = lunchUserSettingsDao;
	}

	@Override
	public void onNewPost(final MicroblogPost microblogPost) {
		try {
			logger.debug("onNewPost");
			final String content = microblogPost.getContent();
			if (isLunch(content)) {
				logger.debug("isLunch = true, sending message");
				final IdentifierIterator<UserIdentifier> i = lunchUserSettingsDao.getActivUserIdentifierIterator();
				while (i.hasNext()) {
					final UserIdentifier userIdentifer = i.next();
					try {
						xmppService.send(userIdentifer, content);
					}
					catch (final XmppServiceException e) {
						logger.debug(e.getClass().getName(), e);
					}
				}
			}
		}
		catch (final StorageException e) {
			logger.debug(e.getClass().getName(), e);
		}
		catch (final IdentifierIteratorException e) {
			logger.debug(e.getClass().getName(), e);
		}
	}

	private boolean isLunch(final String content) {
		if (content != null) {
			for (final String word : words) {
				if (content.indexOf(word) != -1) {
					return true;
				}
			}
		}
		return false;
	}
}
