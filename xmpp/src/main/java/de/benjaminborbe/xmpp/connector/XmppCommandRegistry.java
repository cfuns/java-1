package de.benjaminborbe.xmpp.connector;

import com.google.inject.Singleton;

import de.benjaminborbe.tools.registry.RegistryListenerBase;
import de.benjaminborbe.xmpp.api.XmppCommand;

@Singleton
public class XmppCommandRegistry extends RegistryListenerBase<XmppCommand> {

}
