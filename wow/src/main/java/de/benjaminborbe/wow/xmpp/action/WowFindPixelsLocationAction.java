package de.benjaminborbe.wow.xmpp.action;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import org.slf4j.Logger;

import de.benjaminborbe.tools.image.Coordinate;
import de.benjaminborbe.tools.image.PixelFinder;
import de.benjaminborbe.tools.image.Pixels;
import de.benjaminborbe.tools.util.ThreadResult;
import de.benjaminborbe.vnc.api.VncLocation;
import de.benjaminborbe.vnc.api.VncService;
import de.benjaminborbe.vnc.api.VncServiceException;
import de.benjaminborbe.wow.util.PixelsAdapter;

public class WowFindPixelsLocationAction extends WowActionBase {

	private final ThreadResult<Coordinate> location;

	private final Collection<Pixels> pixels;

	private final int matchPercent;

	private final Logger logger;

	private final VncService vncService;

	private final PixelFinder pixelFinder;

	public WowFindPixelsLocationAction(
			final Logger logger,
			final VncService vncService,
			final PixelFinder pixelFinder,
			final String name,
			final ThreadResult<Boolean> running,
			final ThreadResult<Coordinate> location,
			final Collection<Pixels> pixels,
			final int matchPercent) {
		super(logger, name, running);
		this.logger = logger;
		this.vncService = vncService;
		this.pixelFinder = pixelFinder;
		this.location = location;
		this.pixels = pixels;
		this.matchPercent = matchPercent;
	}

	public WowFindPixelsLocationAction(
			final Logger logger,
			final VncService vncService,
			final PixelFinder pixelFinder,
			final String name,
			final ThreadResult<Boolean> running,
			final ThreadResult<Coordinate> location,
			final Pixels pixels,
			final int matchPercent) {
		this(logger, vncService, pixelFinder, name, running, location, Arrays.asList(pixels), matchPercent);
	}

	@Override
	public void execute() {
		if (validateExecuteResult()) {
			logger.debug(name + " - alread found");
			return;
		}
		logger.debug(name + " - execute started");
		try {
			boolean notFound = true;
			final Iterator<Pixels> i = pixels.iterator();
			while (notFound && i.hasNext()) {
				final Pixels subImage = i.next();
				final Pixels screen = new PixelsAdapter(vncService.getScreenContent().getPixels());
				final Collection<Coordinate> cs = pixelFinder.find(screen, subImage, matchPercent);
				logger.debug("found " + cs.size() + " subImage");
				final Iterator<Coordinate> csi = cs.iterator();
				if (csi.hasNext()) {
					final Coordinate loc = csi.next().add(subImage.getWidth() / 2, subImage.getHeight() / 2);
					logger.debug("found image " + loc);
					location.set(loc);
					notFound = false;
				}
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

	@Override
	public void onFailure() {
		logger.debug(name + " - onFailure - move mouse a bit");
		try {
			final VncLocation loc = vncService.getScreenContent().getPointerLocation();
			final Iterator<Pixels> i = pixels.iterator();
			if (i.hasNext()) {
				final Pixels subImage = i.next();
				vncService.mouseMouse(loc.getX() + subImage.getWidth(), loc.getY() + subImage.getHeight());
			}
		}
		catch (final VncServiceException e) {
			logger.debug(e.getClass().getName(), e);
		}
	}
}
