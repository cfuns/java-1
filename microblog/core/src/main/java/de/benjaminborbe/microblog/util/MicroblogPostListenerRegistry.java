package de.benjaminborbe.microblog.util;

import de.benjaminborbe.microblog.api.MicroblogPostListener;
import de.benjaminborbe.tools.registry.RegistryBase;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class MicroblogPostListenerRegistry extends RegistryBase<MicroblogPostListener> {

	@Inject
	public MicroblogPostListenerRegistry(
		final MicroblogPostListenerMailer microblogPostMailerListener,
		final MicroblogPostListenerIndexer microblogPostListenerIndexer,
		final MicroblogPostListenerNotification microblogPostListenerNotification
	) {
		super(microblogPostMailerListener, microblogPostListenerIndexer, microblogPostListenerNotification);
	}

}
