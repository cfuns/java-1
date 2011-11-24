package de.benjaminborbe.tools.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

import de.benjaminborbe.tools.util.HttpDownloader;
import de.benjaminborbe.tools.util.HttpDownloaderImpl;
import de.benjaminborbe.tools.util.IdGenerator;
import de.benjaminborbe.tools.util.IdGeneratorImpl;
import de.benjaminborbe.tools.util.ThreadRunner;
import de.benjaminborbe.tools.util.ThreadRunnerImpl;

public class ToolModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(ThreadRunner.class).to(ThreadRunnerImpl.class).in(Singleton.class);
		bind(HttpDownloader.class).to(HttpDownloaderImpl.class).in(Singleton.class);
		bind(IdGenerator.class).to(IdGeneratorImpl.class).in(Singleton.class);
	}
}
