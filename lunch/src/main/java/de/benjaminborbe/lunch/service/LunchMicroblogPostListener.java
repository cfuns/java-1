package de.benjaminborbe.lunch.service;

import java.util.Arrays;
import java.util.Collection;

import org.slf4j.Logger;

import com.google.inject.Inject;

import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.lunch.dao.LunchUserSettingsDao;
import de.benjaminborbe.lunch.util.LunchUserNotifier;
import de.benjaminborbe.lunch.util.LunchUserNotifierRegistry;
import de.benjaminborbe.microblog.api.MicroblogPost;
import de.benjaminborbe.microblog.api.MicroblogPostListener;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.tools.IdentifierIterator;
import de.benjaminborbe.storage.tools.IdentifierIteratorException;

public class LunchMicroblogPostListener implements MicroblogPostListener {

	private final Collection<String> words = Arrays.asList("Essen", "Mittagessen", "Mittagstisch");

	private final Logger logger;

	private final LunchUserSettingsDao lunchUserSettingsDao;

	private final LunchUserNotifierRegistry lunchUserNotifierRegistry;

	@Inject
	public LunchMicroblogPostListener(final Logger logger, final LunchUserNotifierRegistry lunchUserNotifierRegistry, final LunchUserSettingsDao lunchUserSettingsDao) {
		this.logger = logger;
		this.lunchUserNotifierRegistry = lunchUserNotifierRegistry;
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
				if (i.hasNext()) {
					while (i.hasNext()) {
						final UserIdentifier userIdentifer = i.next();
						logger.debug("notify user " + userIdentifer);
						for (final LunchUserNotifier lunchUserNotifier : lunchUserNotifierRegistry.getAll()) {
							logger.debug("notify user " + userIdentifer + " via " + lunchUserNotifier.getClass().getSimpleName());
							lunchUserNotifier.notify(userIdentifer, content);
						}
					}
				}
				else {
					logger.debug("no user to notify found");
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
