package de.benjaminborbe.wow.core.xmpp;

import javax.inject.Inject;
import de.benjaminborbe.tools.action.Action;
import de.benjaminborbe.tools.action.ActionChainRunner;
import de.benjaminborbe.tools.image.Coordinate;
import de.benjaminborbe.tools.image.PixelFinder;
import de.benjaminborbe.tools.util.ThreadResult;
import de.benjaminborbe.tools.util.ThreadRunner;
import de.benjaminborbe.vnc.api.VncKey;
import de.benjaminborbe.vnc.api.VncPixels;
import de.benjaminborbe.vnc.api.VncServiceException;
import de.benjaminborbe.wow.core.WowConstants;
import de.benjaminborbe.wow.core.image.WowImageLibrary;
import de.benjaminborbe.wow.core.vnc.WowVncConnector;
import de.benjaminborbe.wow.core.xmpp.action.WowFindBaitLocationAction;
import de.benjaminborbe.wow.core.xmpp.action.WowFindPixelsLocationAction;
import de.benjaminborbe.wow.core.xmpp.action.WowIncreaseCounterAction;
import de.benjaminborbe.wow.core.xmpp.action.WowKeyTypeAction;
import de.benjaminborbe.wow.core.xmpp.action.WowMouseClickAction;
import de.benjaminborbe.wow.core.xmpp.action.WowMouseMoveAction;
import de.benjaminborbe.wow.core.xmpp.action.WowSleepAction;
import de.benjaminborbe.wow.core.xmpp.action.WowTakePixelsAction;
import de.benjaminborbe.wow.core.xmpp.action.WowTakeScreenshotAction;
import de.benjaminborbe.wow.core.xmpp.action.WowWaitOnFishAction;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WowFishingXmppCommand extends WowStartStopXmppCommand {

	private final ThreadResult<Integer> counter = new ThreadResult<>();

	private final ThreadResult<Coordinate> wowAppIconLocation = new ThreadResult<>();

	private final ThreadResult<Coordinate> fishingButtonLocation = new ThreadResult<>();

	private final Logger logger;

	private final WowVncConnector vncService;

	private final ActionChainRunner actionChainRunner;

	private final WowImageLibrary wowImageLibrary;

	private final PixelFinder pixelFinder;

	private final boolean DEBUG = true;

	@Inject
	public WowFishingXmppCommand(
		final Logger logger,
		final PixelFinder pixelFinder,
		final WowVncConnector vncService,
		final ActionChainRunner actionChainRunner,
		final ThreadRunner threadRunner,
		final WowImageLibrary wowImageLibrary) {
		super(logger, threadRunner);
		this.logger = logger;
		this.pixelFinder = pixelFinder;
		this.vncService = vncService;
		this.actionChainRunner = actionChainRunner;
		this.wowImageLibrary = wowImageLibrary;
	}

	@Override
	public void runAction() {
		try {
			final List<Action> actions = new ArrayList<>();
			if (counter.get() % 20 == 0) {
				actions.add(new WowKeyTypeAction(logger, vncService, "jump", running, VncKey.K_SPACE));
			}
			actions.add(new WowIncreaseCounterAction(logger, "increase counter", running, counter));
			actions.add(new WowSleepAction(logger, "sleep", running, 2000));
			// fullscreen
			if (vncService.getScreenContent().getWidth() == 800) {
				wowAppIconLocation.set(new Coordinate(1, 1));
			} else {
				actions.add(new WowFindPixelsLocationAction(logger, vncService, pixelFinder, "find wow app icon", running, wowAppIconLocation, wowImageLibrary.getWowAppIcon(), 60));
			}
			actions.add(new WowFindPixelsLocationAction(logger, vncService, pixelFinder, "find fishing button location", running, fishingButtonLocation, wowImageLibrary
				.getFishingButton(), 60));
			actions.add(new WowMouseMoveAction(logger, vncService, "move mouse to fishing button", running, fishingButtonLocation));
			final ThreadResult<VncPixels> pixelsBeforeFishing = new ThreadResult<>();
			actions.add(new WowTakePixelsAction(logger, vncService, "take pixels before fishing", running, pixelsBeforeFishing));
			if (DEBUG) {
				actions.add(new WowTakeScreenshotAction(logger, vncService, "take screenshot before", "before", running));
			}
			actions.add(new WowMouseClickAction(logger, vncService, "click fishing button", running));
			actions.add(new WowSleepAction(logger, "sleep", running, 2000));
			final ThreadResult<Coordinate> baitLocation = new ThreadResult<>();
			final ThreadResult<VncPixels> pixelsAfterFishing = new ThreadResult<>();
			if (DEBUG) {
				actions.add(new WowTakeScreenshotAction(logger, vncService, "take screenshot after", "after", running));
			}
			actions.add(new WowFindBaitLocationAction(logger, vncService, "find bait", running, pixelsBeforeFishing, pixelsAfterFishing, wowAppIconLocation, baitLocation));
			actions.add(new WowMouseMoveAction(logger, vncService, "move mouse to bait", running, baitLocation, 5, 5));
			actions.add(new WowSleepAction(logger, "sleep", running, 2000));
			actions.add(new WowWaitOnFishAction(logger, vncService, "wait on fish", running, baitLocation));
			actions.add(new WowMouseClickAction(logger, vncService, "click on bait button", running));
			actionChainRunner.run(actions);
		} catch (final IOException | VncServiceException e) {
			logger.debug(e.getClass().getName(), e);
		}
	}

	@Override
	public String getName() {
		return WowConstants.NAME + " fishing";
	}

	@Override
	protected void before() {
		try {
			vncService.connect();
			counter.set(0);
			wowAppIconLocation.set(null);
			fishingButtonLocation.set(null);
		} catch (final VncServiceException e) {
			logger.debug(e.getClass().getName(), e);
		}
	}

	@Override
	protected void after() {
		try {
			counter.set(0);
			wowAppIconLocation.set(null);
			fishingButtonLocation.set(null);
			vncService.disconnect();
		} catch (final VncServiceException e) {
			logger.debug(e.getClass().getName(), e);
		}
	}

}
