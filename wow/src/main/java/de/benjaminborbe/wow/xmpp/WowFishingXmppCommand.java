package de.benjaminborbe.wow.xmpp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;

import com.google.inject.Inject;
import de.benjaminborbe.tools.action.Action;
import de.benjaminborbe.tools.action.ActionChainRunner;
import de.benjaminborbe.tools.image.Coordinate;
import de.benjaminborbe.tools.image.Pixel;
import de.benjaminborbe.tools.image.PixelFinder;
import de.benjaminborbe.tools.image.Pixels;
import de.benjaminborbe.tools.image.PixelsImpl;
import de.benjaminborbe.tools.util.ThreadResult;
import de.benjaminborbe.tools.util.ThreadRunner;
import de.benjaminborbe.vnc.api.VncPixels;
import de.benjaminborbe.vnc.api.VncServiceException;
import de.benjaminborbe.wow.WowConstants;
import de.benjaminborbe.wow.vnc.WowVncConnector;
import de.benjaminborbe.xmpp.api.XmppChat;
import de.benjaminborbe.xmpp.api.XmppChatException;
import de.benjaminborbe.xmpp.api.XmppCommand;

public class WowFishingXmppCommand implements XmppCommand {

	private final ThreadResult<Boolean> running = new ThreadResult<Boolean>(false);

	private class FishingThread implements Runnable {

		private final XmppChat chat;

		private FishingThread(final XmppChat chat) {
			this.chat = chat;
		}

		@Override
		public void run() {
			try {
				vncService.connect();

				while (running.get()) {
					final List<Action> actions = new ArrayList<Action>();
					actions.add(new SleepAction("sleep", 2000));
					final ThreadResult<Coordinate> wowAppIconLocation = new ThreadResult<Coordinate>();
					actions.add(new FindPixelsAction("find wow app icon", wowAppIconLocation, wowImageLibrary.getWowAppIcon(), 70));
					final ThreadResult<Coordinate> fishingButtonLocation = new ThreadResult<Coordinate>();
					actions.add(new FindPixelsAction("find fishing button location", fishingButtonLocation, wowImageLibrary.getFishingButton(), 90));
					actions.add(new MouseMoveAction("move mouse to fishing button", fishingButtonLocation));
					final ThreadResult<VncPixels> pixelsBeforeFishing = new ThreadResult<VncPixels>();
					actions.add(new TakeScreenshotAction("take screenshot before fishing", pixelsBeforeFishing));
					actions.add(new MouseClickAction("click fishing button"));
					actions.add(new SleepAction("sleep", 2000));
					final ThreadResult<Coordinate> baitLocation = new ThreadResult<Coordinate>();
					final ThreadResult<VncPixels> pixelsAfterFishing = new ThreadResult<VncPixels>();
					actions.add(new FindBaitAction("find bait", pixelsBeforeFishing, pixelsAfterFishing, wowAppIconLocation, baitLocation));
					actions.add(new MouseMoveAction("move mouse to bait", baitLocation, 5, 5));
					actions.add(new SleepAction("sleep", 2000));
					actions.add(new WaitOnFishAction("wait on fish", baitLocation));
					actions.add(new MouseClickAction("click on bait button"));
					actionChainRunner.run(actions);
				}
				send(chat, "stopped");
			}
			catch (final VncServiceException e) {
				logger.debug(e.getClass().getName(), e);
			}
			catch (final XmppChatException e) {
				logger.debug(e.getClass().getName(), e);
			}
			catch (final IOException e) {
				logger.debug(e.getClass().getName(), e);
			}
			finally {
				try {
					vncService.disconnect();
				}
				catch (final VncServiceException e) {
					logger.debug(e.getClass().getName(), e);
				}
			}
		}
	}

	private abstract class ActionBase implements Action {

		protected String name;

		public ActionBase(final String name) {
			this.name = name;
		}

		@Override
		public long getWaitTimeout() {
			return 5000;
		}

		@Override
		public long getRetryDelay() {
			return 500;
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
			logger.trace(name + " - validateExecuteResult");
			return running.get();
		}
	}

	private class FindPixelsAction extends ActionBase {

		private final ThreadResult<Coordinate> location;

		private final Pixels pixels;

		private final int matchPercent;

		public FindPixelsAction(final String name, final ThreadResult<Coordinate> location, final Pixels pixels, final int matchPercent) {
			super(name);
			this.location = location;
			this.pixels = pixels;
			this.matchPercent = matchPercent;
		}

		@Override
		public void execute() {
			logger.debug(name + " - execute started");
			try {
				final Pixels screen = new PixelsAdapter(vncService.getScreenContent().getPixels());
				final Collection<Coordinate> cs = pixelFinder.find(screen, pixels, matchPercent);
				logger.debug("found " + cs.size() + " pixels");
				final Iterator<Coordinate> i = cs.iterator();
				if (i.hasNext()) {
					final Coordinate loc = i.next().add(pixels.getWidth() / 2, pixels.getHeight() / 2);
					logger.debug("found image " + loc);
					location.set(loc);
				}
			}
			catch (final VncServiceException e) {
				logger.debug(e.getClass().getName(), e);
			}

			logger.debug(name + " - execute finished");
		}

		@Override
		public boolean validateExecuteResult() {
			logger.debug(name + " - validateExecuteResult");
			return super.validateExecuteResult() && location.get() != null;
		}
	}

	private class SleepAction extends ActionBase {

		private final long sleep;

		public SleepAction(final String name, final long sleep) {
			super(name);
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

	}

	private class WaitOnFishAction extends ActionBase {

		private final ThreadResult<Coordinate> baitLocation;

		private VncPixels vncPixelsOrg;

		public WaitOnFishAction(final String name, final ThreadResult<Coordinate> baitLocation) {
			super(name);
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
		public boolean validateExecuteResult() {
			logger.trace(name + " - validateExecuteResult");
			try {
				if (super.validateExecuteResult() == false)
					return false;
				if (vncPixelsOrg == null)
					return false;

				final int now = vncService.getScreenContent().getPixels().getPixel(baitLocation.get().getX(), baitLocation.get().getY()) & 0x00FF0000;
				final int org = vncPixelsOrg.getPixel(baitLocation.get().getX(), baitLocation.get().getY()) & 0x00FF0000;
				logger.trace(Integer.toHexString(org) + "<=>" + Integer.toHexString(now) + " " + Integer.toHexString(Math.abs(now - org)));
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

		@Override
		public void onFailure() {
			logger.trace(name + " - onFailure");
			try {
				vncService.mouseLeftButtonPress();
				vncService.mouseLeftButtonPress();
			}
			catch (final VncServiceException e) {
				logger.debug(e.getClass().getName(), e);
			}
		}

	}

	private class FindBaitAction extends ActionBase {

		private final ThreadResult<Coordinate> wowAppIconLocation;

		private final ThreadResult<VncPixels> pixelsBeforeFishing;

		private final ThreadResult<VncPixels> pixelsAfterFishing;

		private final ThreadResult<Coordinate> baitLocation;

		private FindBaitAction(
				final String name,
				final ThreadResult<VncPixels> pixelsBeforeFishing,
				final ThreadResult<VncPixels> pixelsAfterFishing,
				final ThreadResult<Coordinate> wowAppIconLocation,
				final ThreadResult<Coordinate> baitLocation) {
			super(name);
			this.pixelsBeforeFishing = pixelsBeforeFishing;
			this.pixelsAfterFishing = pixelsAfterFishing;
			this.wowAppIconLocation = wowAppIconLocation;
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

				final int startX = wowAppIconLocation.get().getX() + 0; // 560; // 1
				final int startY = wowAppIconLocation.get().getY() + 150; // 560; // 1
				final int endX = wowAppIconLocation.get().getX() + 765; // 1300; // width
				final int endY = wowAppIconLocation.get().getY() + 485; // 760; // height

				logger.debug("search in (" + startX + "," + startY + ") to (" + endX + "," + endY + ")");

				for (int x = startX; x <= endX; ++x) {
					for (int y = startY; y <= endY; ++y) {
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
				final int minCounter = 18;
				int bestCounter = minCounter;
				Coordinate bestCoordinate = null;

				for (int x = startX; x <= endX; ++x) {
					for (int y = startY; y <= endY; ++y) {
						final int pixel = diffBlueToRed.getPixel(x, y);
						if (pixel != 0) {
							final int minX = Math.max(startX, x - range);
							final int minY = Math.max(startY, y - range);
							final int maxX = Math.min(endX, x + range);
							final int maxY = Math.min(endY, y + range);
							int counter = 0;
							for (int xc = minX; xc <= maxX; ++xc) {
								for (int yc = minY; yc <= maxY; ++yc) {
									counter += diffBlueToRed.getPixel(xc, yc) & 0x1;
								}
							}
							if (counter >= bestCounter) {
								bestCounter = counter;
								bestCoordinate = new Coordinate(x, y);
							}
						}
					}
				}

				logger.debug("found bait locations at " + bestCoordinate + " with " + bestCounter);
				baitLocation.set(bestCoordinate);
			}
			catch (final VncServiceException e) {
				logger.debug(e.getClass().getName(), e);
			}
			logger.debug(name + " - execute finished");
		}

		@Override
		public boolean validateExecuteResult() {
			logger.debug(name + " - validateExecuteResult");
			return super.validateExecuteResult() && baitLocation.get() != null;
		}

	}

	private class MouseClickAction extends ActionBase {

		public MouseClickAction(final String name) {
			super(name);
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

	}

	private class TakeScreenshotAction extends ActionBase {

		private final ThreadResult<VncPixels> pixelsBeforeFishing;

		private TakeScreenshotAction(final String name, final ThreadResult<VncPixels> pixelsBeforeFishing) {
			super(name);
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
		public boolean validateExecuteResult() {
			logger.debug(name + " - validateExecuteResult");
			return super.validateExecuteResult() && pixelsBeforeFishing.get() != null;
		}
	}

	private class MouseMoveAction extends ActionBase {

		private final ThreadResult<Coordinate> vncLocationThreadResult;

		private final int x;

		private final int y;

		private MouseMoveAction(final String name, final ThreadResult<Coordinate> vncLocationThreadResult) {
			this(name, vncLocationThreadResult, 0, 0);
		}

		private MouseMoveAction(final String name, final ThreadResult<Coordinate> vncLocationThreadResult, final int x, final int y) {
			super(name);
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

	private final ThreadRunner threadRunner;

	private final WowImageLibrary wowImageLibrary;

	private final PixelFinder pixelFinder;

	@Inject
	public WowFishingXmppCommand(
			final Logger logger,
			final PixelFinder pixelFinder,
			final WowVncConnector vncService,
			final ActionChainRunner actionChainRunner,
			final ThreadRunner threadRunner,
			final WowImageLibrary wowImageLibrary) {
		this.logger = logger;
		this.pixelFinder = pixelFinder;
		this.vncService = vncService;
		this.actionChainRunner = actionChainRunner;
		this.threadRunner = threadRunner;
		this.wowImageLibrary = wowImageLibrary;
	}

	@Override
	public String getName() {
		return WowConstants.NAME + " fishing";
	}

	@Override
	public void execute(final XmppChat chat, final String command) {
		logger.debug("execute command " + getName());
		try {

			final String action = parseArgs(command);
			if ("start".equals(action)) {
				if (!running.get()) {
					send(chat, "starting ...");
					running.set(true);
					threadRunner.run("fishing thread", new FishingThread(chat));
					send(chat, "started");
				}
				else {
					send(chat, "already started");
				}
			}
			else if ("stop".equals(action)) {
				if (running.get()) {
					send(chat, "stopping ....");
					running.set(false);
				}
				else {
					send(chat, "already stopped");
				}
			}
			else {
				send(chat, "parameter expected! [start|stop]");
			}
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

	private String parseArgs(final String command) {
		final int pos = command.indexOf(getName()) + getName().length() + 1;
		if (pos > command.length()) {
			return "";
		}
		return command.substring(pos);
	}

	private void send(final XmppChat chat, final String string) throws XmppChatException {
		chat.send(getName() + " - " + string);
		logger.debug(string);
	}
}
