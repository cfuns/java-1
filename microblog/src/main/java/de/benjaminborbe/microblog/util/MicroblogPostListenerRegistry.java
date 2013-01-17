package de.benjaminborbe.microblog.util;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.microblog.api.MicroblogPostListener;
import de.benjaminborbe.tools.registry.RegistryBase;

@Singleton
public class MicroblogPostListenerRegistry extends RegistryBase<MicroblogPostListener> {

	@Inject
	public MicroblogPostListenerRegistry(final MicroblogPostListenerMailer microblogPostMailerListener, final MicroblogPostListenerIndexer microblogPostListenerIndexer) {
		super(microblogPostMailerListener, microblogPostListenerIndexer);
	}

}
