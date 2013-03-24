package de.benjaminborbe.lunch.service;

import java.util.List;

import org.slf4j.Logger;

import com.google.inject.Inject;

import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.lunch.config.LunchConfig;
import de.benjaminborbe.lunch.dao.LunchUserSettingsDao;
import de.benjaminborbe.lunch.util.LunchUserNotifier;
import de.benjaminborbe.lunch.util.LunchUserNotifierException;
import de.benjaminborbe.lunch.util.LunchUserNotifierRegistry;
import de.benjaminborbe.microblog.api.MicroblogPost;
import de.benjaminborbe.microblog.api.MicroblogPostListener;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.tools.IdentifierIterator;
import de.benjaminborbe.storage.tools.IdentifierIteratorException;

public class LunchMicroblogPostListener implements MicroblogPostListener {

	private final Logger logger;

	private final LunchUserSettingsDao lunchUserSettingsDao;

	private final LunchUserNotifierRegistry lunchUserNotifierRegistry;

	private final LunchConfig lunchConfig;

	@Inject
	public LunchMicroblogPostListener(
			final Logger logger,
			final LunchUserNotifierRegistry lunchUserNotifierRegistry,
			final LunchUserSettingsDao lunchUserSettingsDao,
			final LunchConfig lunchConfig) {
		this.logger = logger;
		this.lunchUserNotifierRegistry = lunchUserNotifierRegistry;
		this.lunchUserSettingsDao = lunchUserSettingsDao;
		this.lunchConfig = lunchConfig;
	}

	@Override
	public void onNewPost(final MicroblogPost microblogPost) {
		try {
			if (logger.isTraceEnabled())
				logger.trace("onNewPost");
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
							try {
								lunchUserNotifier.notify(userIdentifer, content);
							}
							catch (final LunchUserNotifierException e) {
								logger.warn("notify failed", e);
							}
						}
					}
				}
				else {
					logger.debug("no user to notify found");
				}
			}
			else {
				if (logger.isTraceEnabled())
					logger.trace("isLunch = false, skip");
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
		if (logger.isTraceEnabled())
			logger.trace("isLunch - content: " + content);
		final List<String> keywords = lunchConfig.getMittagNotifyKeywords();
		if (content != null && keywords != null && !keywords.isEmpty()) {
			final String lowerContent = content.toLowerCase();
			for (final String word : keywords) {
				final String wordTrim = word.trim().toLowerCase();
				if (!wordTrim.isEmpty()) {
					logger.debug("search word: '" + word + "'");
					if (lowerContent.contains(wordTrim)) {
						logger.debug("content match word: '" + word + "'");
						return true;
					}
				}
			}
		}
		return false;
	}
}
