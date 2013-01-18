package de.benjaminborbe.lunch.util;

import com.google.inject.Inject;

import de.benjaminborbe.tools.registry.RegistryBase;

public class LunchUserNotifierRegistry extends RegistryBase<LunchUserNotifier> {

	@Inject
	public LunchUserNotifierRegistry(final LunchUserNotifierMail lunchUserNotifierMail, final LunchUserNotifierXmpp lunchUserNotifierXmpp) {
		super(lunchUserNotifierMail, lunchUserNotifierXmpp);
	}

}
