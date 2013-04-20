package de.benjaminborbe.xmpp.connector;

import javax.inject.Inject;
import javax.inject.Singleton;
import de.benjaminborbe.tools.osgi.service.RegistryServiceTracker;
import de.benjaminborbe.xmpp.api.XmppCommand;
import org.osgi.framework.BundleContext;

@Singleton
public class XmppCommandServiceTracker extends RegistryServiceTracker<XmppCommand> {

	@Inject
	public XmppCommandServiceTracker(final XmppCommandRegistry registry, final BundleContext context, final Class<?> clazz) {
		super(registry, context, clazz);
	}

}
