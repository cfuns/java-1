package de.benjaminborbe.microblog.util;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.benjaminborbe.microblog.api.MicroblogPostListener;
import de.benjaminborbe.tools.registry.RegistryBase;

@Singleton
public class MicroblogPostListenerRegistry extends RegistryBase<MicroblogPostListener> {

	@Inject
	public MicroblogPostListenerRegistry(
			final MicroblogPostListenerMailer microblogPostMailerListener,
			final MicroblogPostListenerIndexer microblogPostListenerIndexer,
			final MicroblogPostListenerNotification microblogPostListenerNotification) {
		super(microblogPostMailerListener, microblogPostListenerIndexer, microblogPostListenerNotification);
	}

}
