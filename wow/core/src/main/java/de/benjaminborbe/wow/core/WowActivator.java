package de.benjaminborbe.wow.core;

import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.BaseBundleActivator;
import de.benjaminborbe.tools.osgi.ServiceInfo;
import de.benjaminborbe.wow.api.WowService;
import de.benjaminborbe.wow.core.guice.WowModules;
import de.benjaminborbe.wow.core.xmpp.WowXmppCommandRegistry;
import de.benjaminborbe.xmpp.api.XmppCommand;
import org.osgi.framework.BundleContext;

import javax.inject.Inject;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class WowActivator extends BaseBundleActivator {

	@Inject
	private WowService wowService;

	@Inject
	private WowXmppCommandRegistry wowCommandRegistry;

	@Override
	protected Modules getModules(final BundleContext context) {
		return new WowModules(context);
	}

	@Override
	public Collection<ServiceInfo> getServiceInfos() {
		final Set<ServiceInfo> result = new HashSet<>(super.getServiceInfos());
		result.add(new ServiceInfo(WowService.class, wowService));
		for (final XmppCommand command : wowCommandRegistry.getAll()) {
			result.add(new ServiceInfo(XmppCommand.class, command, command.getName()));
		}
		return result;
	}

}
