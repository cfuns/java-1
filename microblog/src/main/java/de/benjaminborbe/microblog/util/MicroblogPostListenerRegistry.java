package de.benjaminborbe.microblog.util;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.tools.registry.RegistryBase;

@Singleton
public class MicroblogPostListenerRegistry extends RegistryBase<MicroblogPostListener> {

	@Inject
	public MicroblogPostListenerRegistry(final MicroblogPostListenerMailer microblogPostMailerListener, final MicroblogPostListenerMittagXmpp microblogPostMittagXmppListener) {
		super(microblogPostMailerListener, microblogPostMittagXmppListener);
	}

}
