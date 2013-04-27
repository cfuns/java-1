package de.benjaminborbe.wow.core.xmpp;

import de.benjaminborbe.tools.action.Action;
import de.benjaminborbe.tools.action.ActionChainRunner;
import de.benjaminborbe.tools.image.Coordinate;
import de.benjaminborbe.tools.image.PixelFinder;
import de.benjaminborbe.tools.util.ThreadResult;
import de.benjaminborbe.vnc.api.VncService;
import de.benjaminborbe.wow.core.WowConstants;
import de.benjaminborbe.wow.core.image.WowImageLibrary;
import de.benjaminborbe.wow.core.xmpp.action.WowFindPixelsLocationAction;
import de.benjaminborbe.wow.core.xmpp.action.WowKeyTypeAction;
import de.benjaminborbe.wow.core.xmpp.action.WowMouseClickAction;
import de.benjaminborbe.wow.core.xmpp.action.WowMouseClickDoubleAction;
import de.benjaminborbe.wow.core.xmpp.action.WowMouseMoveAction;
import de.benjaminborbe.wow.core.xmpp.action.WowSleepAction;
import de.benjaminborbe.xmpp.api.XmppChat;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

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
		final WowImageLibrary wowImageLibrary
	) {
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

				final List<Action> actions = new ArrayList<>();
				final ThreadResult<Boolean> running = new ThreadResult<>(true);
				// sleep
				{
					actions.add(new WowSleepAction(logger, "sleep", running, 2000));
				}
				// click desktop icon
				{
					final ThreadResult<Coordinate> startIconLocation = new ThreadResult<>();
					actions.add(new WowFindPixelsLocationAction(logger, vncService, pixelFinder, "find start icon", running, startIconLocation, wowImageLibrary.getWowStartIcon(), 70));
					actions.add(new WowMouseMoveAction(logger, vncService, "move mouse to start icon", running, startIconLocation));
					actions.add(new WowMouseClickDoubleAction(logger, vncService, "double click start icon", running));
				}
				// click play
				{
					final ThreadResult<Coordinate> playButtonLocation = new ThreadResult<>();
					actions.add(new WowFindPixelsLocationAction(logger, vncService, pixelFinder, "find play button", running, playButtonLocation, wowImageLibrary.getWowPlayButton(), 55));
					actions.add(new WowMouseMoveAction(logger, vncService, "move mouse to play button", running, playButtonLocation));
					actions.add(new WowMouseClickAction(logger, vncService, "click play button", running));
				}
				// login
				{
					final ThreadResult<Coordinate> loginButtonLocation = new ThreadResult<>();
					actions.add(new WowFindPixelsLocationAction(logger, vncService, pixelFinder, "find login button", running, loginButtonLocation, wowImageLibrary.getWowLoginButton(), 50));
					// enter email
					{
						// nop
					}
					// select accountname
					{
						final ThreadResult<Coordinate> accountNameDropDownLocation = new ThreadResult<>(new Coordinate(467, 337));
						actions.add(new WowMouseMoveAction(logger, vncService, "move mouse to accountname dropdown button", running, accountNameDropDownLocation));
						actions.add(new WowMouseClickAction(logger, vncService, "click accountname dropdown button", running));
						actions.add(new WowSleepAction(logger, "sleep", running, 1000));
						final ThreadResult<Coordinate> accountNameOptionLocation = new ThreadResult<>(new Coordinate(375, 377));
						actions.add(new WowMouseMoveAction(logger, vncService, "move mouse to accountname option", running, accountNameOptionLocation));
						actions.add(new WowMouseClickAction(logger, vncService, "click accountname option", running));
						actions.add(new WowSleepAction(logger, "sleep", running, 1000));
					}
					// enter password
					{
						final ThreadResult<Coordinate> passwordFieldLocation = new ThreadResult<Coordinate>() {

							@Override
							public Coordinate get() {
								return loginButtonLocation.get().add(0, -48);
							}
						};
						actions.add(new WowMouseMoveAction(logger, vncService, "move mouse to password field", running, passwordFieldLocation));
						actions.add(new WowMouseClickAction(logger, vncService, "click into password field", running));
						actions.add(new WowSleepAction(logger, "sleep", running, 500));
						actions.add(new WowKeyTypeAction(logger, vncService, "type password into field", running, args.get(0)));
					}
					// click login
					{
						actions.add(new WowMouseMoveAction(logger, vncService, "move mouse to login button", running, loginButtonLocation));
						actions.add(new WowSleepAction(logger, "sleep", running, 500));
						actions.add(new WowMouseClickDoubleAction(logger, vncService, "click login button", running));
						actions.add(new WowSleepAction(logger, "sleep", running, 15000));
					}
				}
				// select char
				{
					final ThreadResult<Coordinate> enterWorldLocation = new ThreadResult<>();
					actions.add(new WowFindPixelsLocationAction(logger, vncService, pixelFinder, "find enter world button", running, enterWorldLocation, wowImageLibrary
						.getWowEnterWorldButton(), 60));
					{
						final ThreadResult<Coordinate> charLocation = new ThreadResult<>(new Coordinate(621, 139));
						actions.add(new WowMouseMoveAction(logger, vncService, "move mouse to char location", running, charLocation));
						actions.add(new WowMouseClickAction(logger, vncService, "click char location", running));
						actions.add(new WowSleepAction(logger, "sleep", running, 1000));
					}
					// enter world
					{
						actions.add(new WowMouseMoveAction(logger, vncService, "move mouse to enter world button", running, enterWorldLocation));
						actions.add(new WowMouseClickAction(logger, vncService, "click enter world button", running));
						actions.add(new WowSleepAction(logger, "sleep", running, 500));
					}

				}

				actionChainRunner.run(actions);

			} finally {
				vncService.disconnect();
			}
		} else {
			send(chat, "parameter expected! [username] [password] [account name]");
		}
	}
}
