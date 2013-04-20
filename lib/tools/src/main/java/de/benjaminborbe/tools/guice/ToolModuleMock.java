package de.benjaminborbe.tools.guice;

import javax.inject.Singleton;

import de.benjaminborbe.tools.util.ThreadRunner;
import de.benjaminborbe.tools.util.ThreadRunnerMock;

public class ToolModuleMock extends ToolModuleBase {

	@Override
	protected void configure() {
		super.configure();
		bind(ThreadRunner.class).to(ThreadRunnerMock.class).in(Singleton.class);
	}
}
