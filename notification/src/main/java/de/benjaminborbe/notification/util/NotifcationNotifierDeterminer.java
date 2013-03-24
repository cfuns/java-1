package de.benjaminborbe.notification.util;

import java.util.Arrays;
import java.util.Collection;

import com.google.inject.Inject;

import de.benjaminborbe.authentication.api.UserIdentifier;

public class NotifcationNotifierDeterminer {

	private final NotifcationNotifierMail notifcationNotifierMail;

	private final NotifcationNotifierXmpp notifcationNotifierXmpp;

	@Inject
	public NotifcationNotifierDeterminer(final NotifcationNotifierMail notifcationNotifierMail, final NotifcationNotifierXmpp notifcationNotifierXmpp) {
		this.notifcationNotifierMail = notifcationNotifierMail;
		this.notifcationNotifierXmpp = notifcationNotifierXmpp;
	}

	public Collection<NotifcationNotifier> getNotifcationNotifiers(final UserIdentifier userIdentifier) {
		return Arrays.asList(notifcationNotifierMail, notifcationNotifierXmpp);
	}
}
