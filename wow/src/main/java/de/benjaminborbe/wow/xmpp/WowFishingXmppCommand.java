package de.benjaminborbe.wow.xmpp;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;

import com.google.inject.Inject;

import de.benjaminborbe.tools.action.Action;
import de.benjaminborbe.tools.action.ActionChainRunner;
import de.benjaminborbe.tools.image.Coordinate;
import de.benjaminborbe.tools.image.Pixel;
import de.benjaminborbe.tools.image.Pixels;
import de.benjaminborbe.tools.image.PixelsImpl;
import de.benjaminborbe.tools.util.ThreadResult;
import de.benjaminborbe.vnc.api.VncPixels;
import de.benjaminborbe.vnc.api.VncServiceException;
import de.benjaminborbe.wow.WowConstants;
import de.benjaminborbe.wow.vnc.WowVncConnector;
import de.benjaminborbe.xmpp.api.XmppChat;
import de.benjaminborbe.xmpp.api.XmppChatException;
import de.benjaminborbe.xmpp.api.XmppCommand;

public class WowFishingXmppCommand implements XmppCommand {

	private final class SleepAction extends ActionBase {

		private final String name;

		private final long sleep;

		public SleepAction(final String name, final long sleep) {
			this.name = name;
			this.sleep = sleep;
		}

		@Override
		public void execute() {
			logger.debug(name + " - execute started");
			try {
				Thread.sleep(sleep);
			}
			catch (final InterruptedException e) {
			}
			logger.debug(name + " - execute finished");
		}

		@Override
		public void onSuccess() {
			logger.debug(name + " - onSuccess");
		}

		@Override
		public void onFailure() {
			logger.debug(name + " - onFailure");
		}

		@Override
		public boolean validateExecuteResult() {
			logger.debug(name + " - validateExecuteResult");
			return true;
		}

	}

	private final class WaitOnFishAction extends ActionBase {

		private final String name;

		private final ThreadResult<Coordinate> baitLocation;

		private VncPixels vncPixelsOrg;

		public WaitOnFishAction(final String name, final ThreadResult<Coordinate> baitLocation) {
			this.name = name;
			this.baitLocation = baitLocation;
		}

		@Override
		public void execute() {
			logger.debug(name + " - execute started");
			try {
				vncPixelsOrg = vncService.getScreenContent().getPixels().getCopy();
			}
			catch (final VncServiceException e) {
				logger.debug(e.getClass().getName(), e);
			}
			logger.debug(name + " - execute finished");
		}

		@Override
		public void onSuccess() {
			logger.debug(name + " - onSuccess");
		}

		@Override
		public void onFailure() {
			logger.debug(name + " - onFailure");
		}

		@Override
		public boolean validateExecuteResult() {
			logger.debug(name + " - validateExecuteResult");
			try {
				if (vncPixelsOrg == null)
					return false;

				final int now = vncService.getScreenContent().getPixels().getPixel(baitLocation.get().getX(), baitLocation.get().getY()) & 0x00FF0000;
				final int org = vncPixelsOrg.getPixel(baitLocation.get().getX(), baitLocation.get().getY()) & 0x00FF0000;
				logger.debug(Integer.toHexString(org) + "<=>" + Integer.toHexString(now) + " " + Integer.toHexString(Math.abs(now - org)));
				return Math.abs(now - org) > 0x00400000;
			}
			catch (final VncServiceException e) {
				logger.debug(e.getClass().getName(), e);
				return false;
			}
		}

		@Override
		public long getWaitTimeout() {
			return 15000;
		}

		@Override
		public long getRetryDelay() {
			return 100;
		}

	}

	private final class FindBaitAction extends ActionBase {

		private final ThreadResult<VncPixels> pixelsBeforeFishing;

		private final ThreadResult<VncPixels> pixelsAfterFishing;

		private final ThreadResult<Coordinate> baitLocation;

		private final String name;

		private FindBaitAction(
				final String name,
				final ThreadResult<VncPixels> pixelsBeforeFishing,
				final ThreadResult<VncPixels> pixelsAfterFishing,
				final ThreadResult<Coordinate> baitLocation) {
			this.name = name;
			this.pixelsBeforeFishing = pixelsBeforeFishing;
			this.pixelsAfterFishing = pixelsAfterFishing;
			this.baitLocation = baitLocation;
		}

		@Override
		public void execute() {
			logger.debug(name + " - execute started");
			try {
				final VncPixels before = pixelsBeforeFishing.get();
				final VncPixels after = vncService.getScreenContent().getPixels().getCopy();
				pixelsAfterFishing.set(after);

				vncService.storeVncPixels(before, "before");
				vncService.storeVncPixels(after, "after");

				final int width = before.getWidth();
				final int height = before.getHeight();

				final Pixels diffBlueToRed = new PixelsImpl(new int[width * height], width, height);

				for (int x = 1; x <= width; ++x) {
					for (int y = 1; y <= height; ++y) {
						final Pixel pb = new Pixel(before.getPixel(x, y));
						final Pixel pa = new Pixel(after.getPixel(x, y));
						if (pb.isBlue() && pa.isRed()) {
							diffBlueToRed.setPixel(x, y, 0xFFFFFF);
						}
						else {
							diffBlueToRed.setPixel(x, y, 0x000000);
						}
					}
				}

				final int range = 2;
				final int min = 20;

				final List<Coordinate> cs = new ArrayList<Coordinate>();
				// final Pixels diffNeighbors = new PixelsImpl(new int[width * height], width,
				// height);
				for (int x = 1; x <= width; ++x) {
					for (int y = 1; y <= height; ++y) {
						final int pixel = diffBlueToRed.getPixel(x, y);
						if (pixel == 0) {
							// diffNeighbors.setPixel(x, y, 0x0);
						}
						else {
							final int minX = Math.max(1, x - range);
							final int minY = Math.max(1, y - range);
							final int maxX = Math.min(width, x + range);
							final int maxY = Math.min(height, y + range);
							int counter = 0;
							for (int xc = minX; xc <= maxX; ++xc) {
								for (int yc = minY; yc <= maxY; ++yc) {
									counter += diffBlueToRed.getPixel(xc, yc) & 0x1;
								}
							}
							if (counter >= min) {
								cs.add(new Coordinate(x, y));
								// diffNeighbors.setPixel(x, y, 0xFFFFFF);
							}
							else {
								// diffNeighbors.setPixel(x, y, 0x0);
							}
						}
					}
				}

				logger.debug("found " + cs.size() + " bait locations");
				if (cs.size() > 0) {
					baitLocation.set(cs.get(0));
				}
				// // final List<Coordinate> locations = vncService.diff(before, after);
				// logger.debug("found " + locations.size() + " different locations");
				//
				// for (final Coordinate location : locations) {
				// logger.debug("loc: " + location);
				// }
				// // baitLocation
				// if (locations.size() > 0) {
				// baitLocation.set(locations.get(0));
				// }
			}
			catch (final VncServiceException e) {
				logger.debug(e.getClass().getName(), e);
			}
			logger.debug(name + " - execute finished");
		}

		@Override
		public void onSuccess() {
			logger.debug(name + " - onSuccess");
		}

		@Override
		public void onFailure() {
			logger.debug(name + " - onFailure");
		}

		@Override
		public boolean validateExecuteResult() {
			logger.debug(name + " - validateExecuteResult");
			return baitLocation.get() != null;
		}

	}

	private final class MouseClickAction extends ActionBase {

		private final String name;

		public MouseClickAction(final String name) {
			this.name = name;
		}

		@Override
		public void execute() {
			logger.debug(name + " - execute started");
			try {
				vncService.mouseLeftButtonPress();
				vncService.mouseLeftButtonRelease();
			}
			catch (final VncServiceException e) {
				logger.debug(e.getClass().getName(), e);
			}
			logger.debug(name + " - execute finished");
		}

		@Override
		public void onSuccess() {
			logger.debug(name + " - onSuccess");
		}

		@Override
		public void onFailure() {
			logger.debug(name + " - onFailure");
		}

		@Override
		public boolean validateExecuteResult() {
			logger.debug(name + " - validateExecuteResult");
			return true;
		}
	}

	private final class TakeScreenshotAction extends ActionBase {

		private final ThreadResult<VncPixels> pixelsBeforeFishing;

		private final String name;

		private TakeScreenshotAction(final String name, final ThreadResult<VncPixels> pixelsBeforeFishing) {
			this.name = name;
			this.pixelsBeforeFishing = pixelsBeforeFishing;
		}

		@Override
		public void execute() {
			logger.debug(name + " - execute started");
			try {
				pixelsBeforeFishing.set(vncService.getScreenContent().getPixels().getCopy());
			}
			catch (final VncServiceException e) {
				logger.debug(e.getClass().getName(), e);
			}
			logger.debug(name + " - execute finished");
		}

		@Override
		public void onSuccess() {
			logger.debug(name + " - onSuccess");
		}

		@Override
		public void onFailure() {
			logger.debug(name + " - onFailure");
		}

		@Override
		public boolean validateExecuteResult() {
			logger.debug(name + " - validateExecuteResult");
			return pixelsBeforeFishing.get() != null;
		}
	}

	private final class FindFishingButtonLocationAction extends ActionBase {

		private final ThreadResult<Coordinate> fishingButtonLocation;

		private final String name;

		private FindFishingButtonLocationAction(final String name, final ThreadResult<Coordinate> fishingButtonLocation) {
			this.name = name;
			this.fishingButtonLocation = fishingButtonLocation;
		}

		@Override
		public void execute() {
			logger.debug(name + " - execute started");
			// determine fishing button location
			fishingButtonLocation.set(new Coordinate(1350, 804));
			logger.debug(name + " - execute finished");
		}

		@Override
		public void onSuccess() {
			logger.debug(name + " - onSuccess");
		}

		@Override
		public void onFailure() {
			logger.debug(name + " - onFailure");
		}

		@Override
		public boolean validateExecuteResult() {
			logger.debug(name + " - validateExecuteResult");
			return fishingButtonLocation.get() != null;
		}
	}

	private final class MouseMoveAction extends ActionBase {

		private final ThreadResult<Coordinate> vncLocationThreadResult;

		private final String name;

		private final int x;

		private final int y;

		private MouseMoveAction(final String name, final ThreadResult<Coordinate> vncLocationThreadResult) {
			this(name, vncLocationThreadResult, 0, 0);
		}

		private MouseMoveAction(final String name, final ThreadResult<Coordinate> vncLocationThreadResult, final int x, final int y) {
			this.name = name;
			this.vncLocationThreadResult = vncLocationThreadResult;
			this.x = x;
			this.y = y;
		}

		@Override
		public void execute() {
			logger.debug(name + " - execute started");
			try {
				final Coordinate l = vncLocationThreadResult.get();
				vncService.mouseMouse(l.getX() + x, l.getY() + y);
			}
			catch (final VncServiceException e) {
				logger.debug(e.getClass().getName(), e);
			}
			logger.debug(name + " - execute finished");
		}

		@Override
		public void onSuccess() {
			logger.debug(name + " - onSuccess");
		}

		@Override
		public void onFailure() {
			logger.debug(name + " - onFailure");
		}

		@Override
		public boolean validateExecuteResult() {
			logger.debug(name + " - validateExecuteResult");
			try {
				final Coordinate loc = new Coordinate(vncLocationThreadResult.get().getX() + x, vncLocationThreadResult.get().getY() + y);
				logger.debug("compare location " + loc + " and " + vncService.getScreenContent().getPointerLocation());
				return loc.getX() == vncService.getScreenContent().getPointerLocation().getX() && loc.getY() == vncService.getScreenContent().getPointerLocation().getY();
			}
			catch (final VncServiceException e) {
				logger.debug(e.getClass().getName(), e);
				return false;
			}
		}
	}

	private final Logger logger;

	private final WowVncConnector vncService;

	private final ActionChainRunner actionChainRunner;

	@Inject
	public WowFishingXmppCommand(final Logger logger, final WowVncConnector vncService, final ActionChainRunner actionChainRunner) {
		this.logger = logger;
		this.vncService = vncService;
		this.actionChainRunner = actionChainRunner;
	}

	@Override
	public String getName() {
		return WowConstants.NAME + " fishing";
	}

	@Override
	public void execute(final XmppChat chat, final String msg) {
		logger.debug("execute command " + getName());
		try {
			chat.send(getName() + " - execution started");

			try {
				vncService.connect();

				final List<Action> actions = new ArrayList<Action>();

				final ThreadResult<Coordinate> fishingButtonLocation = new ThreadResult<Coordinate>();
				actions.add(new FindFishingButtonLocationAction("find fishing button location", fishingButtonLocation));
				actions.add(new MouseMoveAction("move mouse to fishing button", fishingButtonLocation));
				final ThreadResult<VncPixels> pixelsBeforeFishing = new ThreadResult<VncPixels>();
				actions.add(new TakeScreenshotAction("take screenshot before fishing", pixelsBeforeFishing));
				actions.add(new MouseClickAction("click fishing button"));
				actions.add(new SleepAction("sleep", 2000));
				final ThreadResult<Coordinate> baitLocation = new ThreadResult<Coordinate>();
				final ThreadResult<VncPixels> pixelsAfterFishing = new ThreadResult<VncPixels>();
				actions.add(new FindBaitAction("find bait", pixelsBeforeFishing, pixelsAfterFishing, baitLocation));
				actions.add(new MouseMoveAction("move mouse to bait", baitLocation, 5, 5));
				actions.add(new SleepAction("sleep", 400));
				actions.add(new WaitOnFishAction("wait on fish", baitLocation));
				actions.add(new MouseClickAction("click on bait button"));

				actionChainRunner.run(actions);

			}
			finally {
				vncService.disconnect();
			}

			chat.send(getName() + " - execution finished");
		}
		catch (final Exception e) {
			try {
				chat.send(getName() + " - execution failed! " + e.getClass().getName());
			}
			catch (final XmppChatException e1) {
				logger.debug(e.getClass().getName(), e);
			}
			logger.debug(e.getClass().getName(), e);
		}
	}

	@Override
	public boolean match(final String body) {
		return body != null && body.indexOf(getName()) != -1;
	}
}
