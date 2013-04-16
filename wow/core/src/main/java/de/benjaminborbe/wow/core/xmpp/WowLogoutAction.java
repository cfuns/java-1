package de.benjaminborbe.wow.core.xmpp;

import com.google.inject.Inject;
import de.benjaminborbe.tools.action.Action;
import de.benjaminborbe.tools.action.ActionChainRunner;
import de.benjaminborbe.tools.util.ThreadResult;
import de.benjaminborbe.vnc.api.VncService;
import de.benjaminborbe.wow.core.WowConstants;
import de.benjaminborbe.wow.core.xmpp.action.WowAltF4Action;
import de.benjaminborbe.wow.core.xmpp.action.WowSleepAction;
import de.benjaminborbe.xmpp.api.XmppChat;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class WowLogoutAction extends WowBaseXmppCommand {

	private final ActionChainRunner actionChainRunner;

	private final Logger logger;

	private final VncService vncService;

	@Inject
	public WowLogoutAction(final Logger logger, final ActionChainRunner actionChainRunner, final VncService vncService) {
		super(logger);
		this.logger = logger;
		this.actionChainRunner = actionChainRunner;
		this.vncService = vncService;
	}

	@Override
	public String getName() {
		return WowConstants.NAME + " logout";
	}

	@Override
	public void executeInternal(final XmppChat chat, final String command) throws Exception {

		try {
			vncService.connect();

			final List<Action> actions = new ArrayList<>();
			final ThreadResult<Boolean> running = new ThreadResult<>(true);
			// sleep
			{
				actions.add(new WowSleepAction(logger, "sleep", running, 2000));
			}
			// ALT+F4
			{
				actions.add(new WowAltF4Action(logger, "press ALT+F4", running, vncService));
			}
			actionChainRunner.run(actions);
		} finally {
			vncService.disconnect();
		}

	}

}
