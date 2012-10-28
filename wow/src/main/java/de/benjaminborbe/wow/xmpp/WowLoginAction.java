package de.benjaminborbe.wow.xmpp;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;

import com.google.inject.Inject;

import de.benjaminborbe.tools.action.Action;
import de.benjaminborbe.tools.action.ActionChainRunner;
import de.benjaminborbe.tools.image.Coordinate;
import de.benjaminborbe.tools.image.PixelFinder;
import de.benjaminborbe.tools.util.ThreadResult;
import de.benjaminborbe.vnc.api.VncService;
import de.benjaminborbe.wow.WowConstants;
import de.benjaminborbe.wow.image.WowImageLibrary;
import de.benjaminborbe.wow.xmpp.action.WowFindPixelsLocationAction;
import de.benjaminborbe.wow.xmpp.action.WowKeyTypeAction;
import de.benjaminborbe.wow.xmpp.action.WowMouseClickAction;
import de.benjaminborbe.wow.xmpp.action.WowMouseClickDoubleAction;
import de.benjaminborbe.wow.xmpp.action.WowMouseMoveAction;
import de.benjaminborbe.wow.xmpp.action.WowSleepAction;
import de.benjaminborbe.xmpp.api.XmppChat;

public class WowLoginAction extends WowBaseXmppCommand {

	private final ActionChainRunner actionChainRunner;

	private final Logger logger;

	private final VncService vncService;

	private final PixelFinder pixelFinder;

	private final WowImageLibrary wowImageLibrary;

	@Inject
	public WowLoginAction(
			final Logger logger,
			final ActionChainRunner actionChainRunner,
			final VncService vncService,
			final PixelFinder pixelFinder,
			final WowImageLibrary wowImageLibrary) {
		super(logger);
		this.logger = logger;
		this.actionChainRunner = actionChainRunner;
		this.vncService = vncService;
		this.pixelFinder = pixelFinder;
		this.wowImageLibrary = wowImageLibrary;
	}

	@Override
	public String getName() {
		return WowConstants.NAME + " login";
	}

	@Override
	public void executeInternal(final XmppChat chat, final String command) throws Exception {

		final List<String> args = parseArgs(command);
		if (args.size() == 1) {

			try {
				vncService.connect();

				final List<Action> actions = new ArrayList<Action>();
				final ThreadResult<Boolean> running = new ThreadResult<Boolean>(true);
				// sleep
				{
					actions.add(new WowSleepAction(logger, "sleep", running, 2000));
				}
				// click desktop icon
				{
					final ThreadResult<Coordinate> startIconLocation = new ThreadResult<Coordinate>();
					actions.add(new WowFindPixelsLocationAction(logger, vncService, pixelFinder, "find start icon", running, startIconLocation, wowImageLibrary.getWowStartIcon(), 70));
					actions.add(new WowMouseMoveAction(logger, vncService, "move mouse to start icon", running, startIconLocation));
					actions.add(new WowMouseClickDoubleAction(logger, vncService, "double click start icon", running));
				}
				// click play
				{
					final ThreadResult<Coordinate> playButtonLocation = new ThreadResult<Coordinate>();
					actions.add(new WowFindPixelsLocationAction(logger, vncService, pixelFinder, "find play button", running, playButtonLocation, wowImageLibrary.getWowPlayButton(), 70));
					actions.add(new WowMouseMoveAction(logger, vncService, "move mouse to play button", running, playButtonLocation));
					actions.add(new WowMouseClickAction(logger, vncService, "click play button", running));
				}
				// login
				{
					final ThreadResult<Coordinate> playButtonLocation = new ThreadResult<Coordinate>();
					actions.add(new WowFindPixelsLocationAction(logger, vncService, pixelFinder, "find login button", running, playButtonLocation, wowImageLibrary.getWowLoginButton(), 70));
					// enter email
					// select accountname
					// enter password
					{
						actions.add(new WowMouseMoveAction(logger, vncService, "move mouse to password field", running, playButtonLocation));
						actions.add(new WowMouseClickAction(logger, vncService, "click into password field", running));
						actions.add(new WowSleepAction(logger, "sleep", running, 100));
						actions.add(new WowKeyTypeAction(logger, vncService, "type password into field", running, args.get(0)));
					}
					// click login
					{
						actions.add(new WowMouseMoveAction(logger, vncService, "move mouse to login button", running, playButtonLocation));
						actions.add(new WowMouseClickAction(logger, vncService, "click login button", running));
					}
				}

				actionChainRunner.run(actions);

			}
			finally {
				vncService.disconnect();
			}
		}
		else {
			send(chat, "parameter expected! [username] [password] [account name]");
		}
	}
}
