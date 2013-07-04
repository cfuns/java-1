package de.benjaminborbe.tools.guice;

import de.benjaminborbe.tools.util.ThreadRunner;
import de.benjaminborbe.tools.util.ThreadRunnerImpl;

import javax.inject.Singleton;

public class ToolModule extends ToolModuleBase {

	@Override
	protected void configure() {
		super.configure();
		bind(ThreadRunner.class).to(ThreadRunnerImpl.class).in(Singleton.class);
	}
}
