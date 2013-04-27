package de.benjaminborbe.wow.core.vnc;

import de.benjaminborbe.vnc.api.VncLocation;
import de.benjaminborbe.vnc.api.VncService;
import de.benjaminborbe.vnc.api.VncServiceAdapter;
import de.benjaminborbe.vnc.api.VncServiceException;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class WowVncConnector extends VncServiceAdapter {

	private final WowVncMouseMover wowVncMouseMover;

	@Inject
	public WowVncConnector(final VncService vncService, final WowVncMouseMover wowVncMouseMover) {
		super(vncService);
		this.wowVncMouseMover = wowVncMouseMover;
	}

	@Override
	public void mouseMouse(final int x, final int y) throws VncServiceException {
		wowVncMouseMover.mouseMouse(x, y);
	}

	@Override
	public void mouseMouse(final VncLocation location) throws VncServiceException {
		wowVncMouseMover.mouseMouse(location);
	}

}
