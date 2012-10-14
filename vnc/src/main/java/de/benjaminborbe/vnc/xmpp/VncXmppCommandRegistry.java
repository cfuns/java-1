package de.benjaminborbe.vnc.xmpp;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.tools.registry.RegistryBase;
import de.benjaminborbe.xmpp.api.XmppCommand;

@Singleton
public class VncXmppCommandRegistry extends RegistryBase<XmppCommand> {

	@Inject
	public VncXmppCommandRegistry(
			final VncServiceDisconnectXmppCommand vncServiceDisconnectXmppCommand,
			final VncServiceColorPickerXmppCommand vncServiceColorPickerXmppCommand,
			final VncServiceMouseMoveXmppCommand vncServiceMouseMoveXmppCommand) {
		super(vncServiceDisconnectXmppCommand, vncServiceColorPickerXmppCommand, vncServiceMouseMoveXmppCommand);
	}

}
