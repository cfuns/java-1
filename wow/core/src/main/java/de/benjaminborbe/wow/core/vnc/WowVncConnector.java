package de.benjaminborbe.wow.core.vnc;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import de.benjaminborbe.vnc.api.VncLocation;
import de.benjaminborbe.vnc.api.VncService;
import de.benjaminborbe.vnc.api.VncServiceAdapter;
import de.benjaminborbe.vnc.api.VncServiceException;

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
