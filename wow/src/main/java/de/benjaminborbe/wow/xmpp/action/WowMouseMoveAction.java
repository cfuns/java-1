package de.benjaminborbe.wow.xmpp.action;

import org.slf4j.Logger;

import de.benjaminborbe.tools.image.Coordinate;
import de.benjaminborbe.tools.util.ThreadResult;
import de.benjaminborbe.vnc.api.VncService;
import de.benjaminborbe.vnc.api.VncServiceException;

public class WowMouseMoveAction extends WowActionBase {

	private final ThreadResult<Coordinate> vncLocationThreadResult;

	private final int x;

	private final int y;

	private final Logger logger;

	private final VncService vncService;

	public WowMouseMoveAction(
			final Logger logger,
			final VncService vncService,
			final String name,
			final ThreadResult<Boolean> running,
			final ThreadResult<Coordinate> vncLocationThreadResult) {
		this(logger, vncService, name, running, vncLocationThreadResult, 0, 0);
	}

	public WowMouseMoveAction(
			final Logger logger,
			final VncService vncService,
			final String name,
			final ThreadResult<Boolean> running,
			final ThreadResult<Coordinate> vncLocationThreadResult,
			final int x,
			final int y) {
		super(logger, name, running);
		this.vncService = vncService;
		this.logger = logger;
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
