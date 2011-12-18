package de.benjaminborbe.slash;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.osgi.framework.BundleContext;
import com.google.inject.Inject;
import de.benjaminborbe.slash.guice.SlashModules;
import de.benjaminborbe.slash.servlet.SlashServlet;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.HttpBundleActivator;
import de.benjaminborbe.tools.osgi.ServletInfo;

public class SlashActivator extends HttpBundleActivator {

	@Inject
	private SlashServlet slashServlet;

	public SlashActivator() {
		super("");
	}

	@Override
	protected Modules getModules(final BundleContext context) {
		return new SlashModules(context);
	}

	@Override
	protected Collection<ServletInfo> getServletInfos() {
		final Set<ServletInfo> result = new HashSet<ServletInfo>(super.getServletInfos());
		result.add(new ServletInfo(slashServlet, "/"));
		return result;
	}

}
