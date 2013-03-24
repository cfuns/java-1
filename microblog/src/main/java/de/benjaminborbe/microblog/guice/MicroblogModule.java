package de.benjaminborbe.microblog.guice;

import org.slf4j.Logger;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

import de.benjaminborbe.microblog.api.MicroblogService;
import de.benjaminborbe.microblog.config.MicroblogConfig;
import de.benjaminborbe.microblog.config.MicroblogConfigImpl;
import de.benjaminborbe.microblog.connector.MicroblogConnector;
import de.benjaminborbe.microblog.connector.MicroblogConnectorImpl;
import de.benjaminborbe.microblog.conversation.MicroblogConversationFinder;
import de.benjaminborbe.microblog.conversation.MicroblogConversationFinderImpl;
import de.benjaminborbe.microblog.conversation.MicroblogConversationNotifier;
import de.benjaminborbe.microblog.conversation.MicroblogConversationNotifierImpl;
import de.benjaminborbe.microblog.post.MicroblogPostNotifier;
import de.benjaminborbe.microblog.post.MicroblogPostNotifierImpl;
import de.benjaminborbe.microblog.revision.MicroblogRevisionStorage;
import de.benjaminborbe.microblog.revision.MicroblogRevisionStorageImpl;
import de.benjaminborbe.microblog.service.MicroblogServiceImpl;
import de.benjaminborbe.tools.log.LoggerSlf4Provider;

public class MicroblogModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(MicroblogConfig.class).to(MicroblogConfigImpl.class).in(Singleton.class);
		bind(MicroblogConversationFinder.class).to(MicroblogConversationFinderImpl.class).in(Singleton.class);
		bind(MicroblogService.class).to(MicroblogServiceImpl.class).in(Singleton.class);
		bind(MicroblogPostNotifier.class).to(MicroblogPostNotifierImpl.class).in(Singleton.class);
		bind(MicroblogConversationNotifier.class).to(MicroblogConversationNotifierImpl.class).in(Singleton.class);
		bind(MicroblogConnector.class).to(MicroblogConnectorImpl.class).in(Singleton.class);
		bind(MicroblogRevisionStorage.class).to(MicroblogRevisionStorageImpl.class).in(Singleton.class);
		bind(Logger.class).toProvider(LoggerSlf4Provider.class).in(Singleton.class);
	}
}
