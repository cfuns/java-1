package de.benjaminborbe.wow.xmpp.action;

import org.slf4j.Logger;

import de.benjaminborbe.tools.image.Coordinate;
import de.benjaminborbe.tools.image.Pixel;
import de.benjaminborbe.tools.image.Pixels;
import de.benjaminborbe.tools.image.PixelsImpl;
import de.benjaminborbe.tools.util.ThreadResult;
import de.benjaminborbe.vnc.api.VncPixels;
import de.benjaminborbe.vnc.api.VncService;
import de.benjaminborbe.vnc.api.VncServiceException;

public class WowFindBaitAction extends WowActionBase {

	private final ThreadResult<Coordinate> wowAppIconLocation;

	private final ThreadResult<VncPixels> pixelsBeforeFishing;

	private final ThreadResult<VncPixels> pixelsAfterFishing;

	private final ThreadResult<Coordinate> baitLocation;

	private final Logger logger;

	private VncService vncService;

	public WowFindBaitAction(
			final Logger logger,
			final VncService vncService,
			final String name,
			final ThreadResult<Boolean> running,
			final ThreadResult<VncPixels> pixelsBeforeFishing,
			final ThreadResult<VncPixels> pixelsAfterFishing,
			final ThreadResult<Coordinate> wowAppIconLocation,
			final ThreadResult<Coordinate> baitLocation) {
		super(logger, name, running);
		this.logger = logger;
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
			if (bestCoordinate != null) {
				logger.debug("found bait locations at " + bestCoordinate + " with " + bestCounter);
				baitLocation.set(bestCoordinate);
			}
			else {
				logger.debug("bait location not found");
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
		return super.validateExecuteResult() && baitLocation.get() != null;
	}

}
