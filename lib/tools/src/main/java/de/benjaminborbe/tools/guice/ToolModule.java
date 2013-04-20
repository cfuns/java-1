package de.benjaminborbe.tools.guice;

import javax.inject.Singleton;

import de.benjaminborbe.tools.util.ThreadRunner;
import de.benjaminborbe.tools.util.ThreadRunnerImpl;

public class ToolModule extends ToolModuleBase {

	@Override
	protected void configure() {
		super.configure();
		bind(ThreadRunner.class).to(ThreadRunnerImpl.class).in(Singleton.class);
	}
}
