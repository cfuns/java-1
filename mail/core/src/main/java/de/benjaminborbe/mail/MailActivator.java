package de.benjaminborbe.mail;

import de.benjaminborbe.mail.api.MailService;
import de.benjaminborbe.mail.guice.MailModules;
import de.benjaminborbe.mail.service.MailMessageConsumer;
import de.benjaminborbe.message.api.MessageConsumer;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.BaseBundleActivator;
import de.benjaminborbe.tools.osgi.ServiceInfo;
import org.osgi.framework.BundleContext;

import javax.inject.Inject;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class MailActivator extends BaseBundleActivator {

	@Inject
	private MailService mailService;

	@Inject
	private MailMessageConsumer mailMessageConsumer;

	@Override
	protected Modules getModules(final BundleContext context) {
		return new MailModules(context);
	}

	@Override
	public Collection<ServiceInfo> getServiceInfos() {
		final Set<ServiceInfo> result = new HashSet<>(super.getServiceInfos());
		result.add(new ServiceInfo(MailService.class, mailService));
		result.add(new ServiceInfo(MessageConsumer.class, mailMessageConsumer));
		return result;
	}

}
