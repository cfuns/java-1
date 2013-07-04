package de.benjaminborbe.tools.action;

import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class ActionChainRunner {

	private final class A extends ActionAdapter {

		private final List<Action> actions;

		private final int pos;

		private A(final Action action, final List<Action> actions, final int pos) {
			super(action);
			this.actions = actions;
			this.pos = pos;
		}

		@Override
		public void onSuccess() {
			super.onSuccess();
			run(actions, pos + 1);
		}
	}

	private final Logger logger;

	private final ActionRunner actionRunner;

	@Inject
	public ActionChainRunner(final Logger logger, final ActionRunner actionRunner) {
		this.logger = logger;
		this.actionRunner = actionRunner;
	}

	public void run(final List<Action> actions) {
		logger.debug("run actions - size: " + actions.size());
		run(actions, 0);
	}

	private void run(final List<Action> actions, final int pos) {
		if (actions.size() == pos)
			return;
		logger.debug("run action " + (pos + 1) + "/" + actions.size());
		actionRunner.run(new A(actions.get(pos), actions, pos));
	}
}
