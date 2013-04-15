package de.benjaminborbe.lunch.service;

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
import de.benjaminborbe.tools.date.CalendarUtil;
import org.slf4j.Logger;

import java.util.Calendar;
import java.util.List;

public class LunchMicroblogPostListener implements MicroblogPostListener {

	private static final long MAX_AGE = 30l * 60l * 1000l; // 30min

	private final Logger logger;

	private final LunchUserSettingsDao lunchUserSettingsDao;

	private final CalendarUtil calendarUtil;

	private final LunchUserNotifierRegistry lunchUserNotifierRegistry;

	private final LunchConfig lunchConfig;

	@Inject
	public LunchMicroblogPostListener(
		final Logger logger,
		final CalendarUtil calendarUtil,
		final LunchUserNotifierRegistry lunchUserNotifierRegistry,
		final LunchUserSettingsDao lunchUserSettingsDao,
		final LunchConfig lunchConfig) {
		this.logger = logger;
		this.calendarUtil = calendarUtil;
		this.lunchUserNotifierRegistry = lunchUserNotifierRegistry;
		this.lunchUserSettingsDao = lunchUserSettingsDao;
		this.lunchConfig = lunchConfig;
	}

	@Override
	public void onNewPost(final MicroblogPost microblogPost) {
		try {
			if (logger.isTraceEnabled())
				logger.trace("onNewPost");
			if (isLunch(microblogPost)) {
				logger.debug("isLunch = true, sending message");
				final IdentifierIterator<UserIdentifier> i = lunchUserSettingsDao.getActivUserIdentifierIterator();
				if (i.hasNext()) {
					while (i.hasNext()) {
						final UserIdentifier userIdentifer = i.next();
						logger.debug("notify user " + userIdentifer);
						for (final LunchUserNotifier lunchUserNotifier : lunchUserNotifierRegistry.getAll()) {
							logger.debug("notify user " + userIdentifer + " via " + lunchUserNotifier.getClass().getSimpleName());
							try {
								lunchUserNotifier.notify(userIdentifer, microblogPost.getContent());
							} catch (final LunchUserNotifierException e) {
								logger.warn("notify failed", e);
							}
						}
					}
				} else {
					logger.debug("no user to notify found");
				}
			} else {
				if (logger.isTraceEnabled())
					logger.trace("isLunch = false, skip");
			}
		} catch (final StorageException | IdentifierIteratorException e) {
			logger.debug(e.getClass().getName(), e);
		}
	}

	private boolean isLunch(final MicroblogPost microblogPost) {
		final Calendar calendar = microblogPost.getDate();
		final String content = microblogPost.getContent();
		final Calendar now = calendarUtil.now();
		final long diff = now.getTimeInMillis() - calendar.getTimeInMillis();
		if (logger.isDebugEnabled()) {
			logger.debug("isLunch - content: " + content + " date: " + calendarUtil.toDateTimeZoneString(calendar) + " now: " + calendarUtil.toDateTimeZoneString(now) + " diff: " + diff);
		}
		if (diff > MAX_AGE) {
			logger.debug(diff + " > " + MAX_AGE + " skip");
			return false;
		}

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
