package de.benjaminborbe.mail;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.osgi.framework.BundleContext;

import com.google.inject.Inject;

import de.benjaminborbe.mail.api.MailService;
import de.benjaminborbe.mail.guice.MailModules;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.BaseBundleActivator;
import de.benjaminborbe.tools.osgi.ServiceInfo;

public class MailActivator extends BaseBundleActivator {

	@Inject
	private MailService mailService;

	@Override
	protected Modules getModules(final BundleContext context) {
		return new MailModules(context);
	}

	@Override
	public Collection<ServiceInfo> getServiceInfos() {
		final Set<ServiceInfo> result = new HashSet<ServiceInfo>(super.getServiceInfos());
		result.add(new ServiceInfo(MailService.class, mailService));
		return result;
	}

}
