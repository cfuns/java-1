package de.benjaminborbe.xmpp.connector;

import org.osgi.framework.BundleContext;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.tools.osgi.service.RegistryServiceTracker;
import de.benjaminborbe.xmpp.api.XmppCommand;

@Singleton
public class XmppCommandServiceTracker extends RegistryServiceTracker<XmppCommand> {

	@Inject
	public XmppCommandServiceTracker(final XmppCommandRegistry registry, final BundleContext context, final Class<?> clazz) {
		super(registry, context, clazz);
	}

}
