package de.benjaminborbe.wow.xmpp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;

import com.google.inject.Inject;
import de.benjaminborbe.tools.action.Action;
import de.benjaminborbe.tools.action.ActionChainRunner;
import de.benjaminborbe.tools.image.Coordinate;
import de.benjaminborbe.tools.image.PixelFinder;
import de.benjaminborbe.tools.util.ThreadResult;
import de.benjaminborbe.tools.util.ThreadRunner;
import de.benjaminborbe.vnc.api.VncKey;
import de.benjaminborbe.vnc.api.VncPixels;
import de.benjaminborbe.vnc.api.VncServiceException;
import de.benjaminborbe.wow.WowConstants;
import de.benjaminborbe.wow.image.WowImageLibrary;
import de.benjaminborbe.wow.vnc.WowVncConnector;
import de.benjaminborbe.wow.xmpp.action.WowFindBaitLocationAction;
import de.benjaminborbe.wow.xmpp.action.WowFindPixelsLocationAction;
import de.benjaminborbe.wow.xmpp.action.WowIncreaseCounterAction;
import de.benjaminborbe.wow.xmpp.action.WowMouseClickAction;
import de.benjaminborbe.wow.xmpp.action.WowMouseMoveAction;
import de.benjaminborbe.wow.xmpp.action.WowSendKeyAction;
import de.benjaminborbe.wow.xmpp.action.WowSleepAction;
import de.benjaminborbe.wow.xmpp.action.WowTakePixelsAction;
import de.benjaminborbe.wow.xmpp.action.WowTakeScreenshotAction;
import de.benjaminborbe.wow.xmpp.action.WowWaitOnFishAction;

public class WowFishingXmppCommand extends WowStartStopXmppCommand {

	private final ThreadResult<Integer> counter = new ThreadResult<Integer>();

	private final ThreadResult<Coordinate> wowAppIconLocation = new ThreadResult<Coordinate>();

	private final ThreadResult<Coordinate> fishingButtonLocation = new ThreadResult<Coordinate>();

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
			final List<Action> actions = new ArrayList<Action>();
			if (counter.get() % 20 == 0) {
				actions.add(new WowSendKeyAction(logger, vncService, "jump", running, VncKey.K_SPACE));
			}
			actions.add(new WowIncreaseCounterAction(logger, "increase counter", running, counter));
			actions.add(new WowSleepAction(logger, "sleep", running, 2000));
			actions.add(new WowFindPixelsLocationAction(logger, vncService, pixelFinder, "find wow app icon", running, wowAppIconLocation, Arrays.asList(wowImageLibrary.getWowAppIconV1(),
					wowImageLibrary.getWowAppIconV2()), 70));
			actions.add(new WowFindPixelsLocationAction(logger, vncService, pixelFinder, "find fishing button location", running, fishingButtonLocation, wowImageLibrary.getFishingButton(), 70));
			actions.add(new WowMouseMoveAction(logger, vncService, "move mouse to fishing button", running, fishingButtonLocation));
			final ThreadResult<VncPixels> pixelsBeforeFishing = new ThreadResult<VncPixels>();
			actions.add(new WowTakePixelsAction(logger, vncService, "take pixels before fishing", running, pixelsBeforeFishing));
			if (DEBUG) {
				actions.add(new WowTakeScreenshotAction(logger, vncService, "take screenshot before", "before", running));
			}
			actions.add(new WowMouseClickAction(logger, vncService, "click fishing button", running));
			actions.add(new WowSleepAction(logger, "sleep", running, 2000));
			final ThreadResult<Coordinate> baitLocation = new ThreadResult<Coordinate>();
			final ThreadResult<VncPixels> pixelsAfterFishing = new ThreadResult<VncPixels>();
			if (DEBUG) {
				actions.add(new WowTakeScreenshotAction(logger, vncService, "take screenshot after", "after", running));
			}
			actions.add(new WowFindBaitLocationAction(logger, vncService, "find bait", running, pixelsBeforeFishing, pixelsAfterFishing, wowAppIconLocation, baitLocation));
			actions.add(new WowMouseMoveAction(logger, vncService, "move mouse to bait", running, baitLocation, 5, 5));
			actions.add(new WowSleepAction(logger, "sleep", running, 2000));
			actions.add(new WowWaitOnFishAction(logger, vncService, "wait on fish", running, baitLocation));
			actions.add(new WowMouseClickAction(logger, vncService, "click on bait button", running));
			actionChainRunner.run(actions);
		}
		catch (final IOException e) {
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
		}
		catch (final VncServiceException e) {
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
		}
		catch (final VncServiceException e) {
			logger.debug(e.getClass().getName(), e);
		}
	}

}
