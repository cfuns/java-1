package de.benjaminborbe.microblog.util;

import javax.inject.Inject;
import javax.inject.Singleton;
import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.tools.map.MapList;

import java.util.Collection;

@Singleton
public class MicroblogNotification {

	private final MapList<UserIdentifier, String> keywords = new MapList<>();

	@Inject
	public MicroblogNotification() {
	}

	public Collection<String> listNotifications(final UserIdentifier userIdentifier) {
		return keywords.get(userIdentifier);
	}

	public void activateNotification(final UserIdentifier userIdentifier, final String keyword) {
		keywords.add(userIdentifier, keyword);
	}

	public void deactivateNotification(final UserIdentifier userIdentifier, final String keyword) {
		keywords.remove(userIdentifier, keyword);
	}

	public Collection<UserIdentifier> getUsersWithNotifications() {
		return keywords.keySet();
	}
}