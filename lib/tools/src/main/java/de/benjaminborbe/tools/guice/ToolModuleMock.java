package de.benjaminborbe.tools.guice;

import de.benjaminborbe.tools.util.ThreadRunner;
import de.benjaminborbe.tools.util.ThreadRunnerMock;

import javax.inject.Singleton;

public class ToolModuleMock extends ToolModuleBase {

	@Override
	protected void configure() {
		super.configure();
		bind(ThreadRunner.class).to(ThreadRunnerMock.class).in(Singleton.class);
	}
}
