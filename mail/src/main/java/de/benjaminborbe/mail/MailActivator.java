package de.benjaminborbe.mail;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.osgi.framework.BundleContext;

import com.google.inject.Inject;

import de.benjaminborbe.mail.api.MailService;
import de.benjaminborbe.mail.guice.MailModules;
import de.benjaminborbe.mail.servlet.MailLogFilter;
import de.benjaminborbe.mail.servlet.MailServlet;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.FilterInfo;
import de.benjaminborbe.tools.osgi.HttpBundleActivator;
import de.benjaminborbe.tools.osgi.ServiceInfo;
import de.benjaminborbe.tools.osgi.ServletInfo;

public class MailActivator extends HttpBundleActivator {

	@Inject
	private MailServlet mailServlet;

	@Inject
	private MailService mailService;

	@Inject
	private MailLogFilter mailLogFilter;

	public MailActivator() {
		super("mail");
	}

	@Override
	protected Modules getModules(final BundleContext context) {
		return new MailModules(context);
	}

	@Override
	protected Collection<ServletInfo> getServletInfos() {
		final Set<ServletInfo> result = new HashSet<ServletInfo>(super.getServletInfos());
		result.add(new ServletInfo(mailServlet, "/"));
		return result;
	}

	@Override
	protected Collection<ServiceInfo> getServiceInfos() {
		final Set<ServiceInfo> result = new HashSet<ServiceInfo>(super.getServiceInfos());
		result.add(new ServiceInfo(MailService.class, mailService));
		return result;
	}

	@Override
	protected Collection<FilterInfo> getFilterInfos() {
		final Set<FilterInfo> result = new HashSet<FilterInfo>(super.getFilterInfos());
		result.add(new FilterInfo(mailLogFilter, ".*", 1));
		return result;
	}

}
