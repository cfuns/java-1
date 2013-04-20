package de.benjaminborbe.xmpp.connector;

import javax.inject.Singleton;
import de.benjaminborbe.tools.registry.RegistryListenerBase;
import de.benjaminborbe.xmpp.api.XmppCommand;

@Singleton
public class XmppCommandRegistry extends RegistryListenerBase<XmppCommand> {

}
