package de.benjaminborbe.sample.gui;

import javax.inject.Inject;
import de.benjaminborbe.sample.gui.guice.SampleGuiModules;
import de.benjaminborbe.sample.gui.servlet.SampleGuiServlet;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.HttpBundleActivator;
import de.benjaminborbe.tools.osgi.ServletInfo;
import org.osgi.framework.BundleContext;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class SampleGuiActivator extends HttpBundleActivator {

	@Inject
	private SampleGuiServlet sampleGuiServlet;

	public SampleGuiActivator() {
		super(SampleGuiConstants.NAME);
	}

	@Override
	protected Modules getModules(final BundleContext context) {
		return new SampleGuiModules(context);
	}

	@Override
	protected Collection<ServletInfo> getServletInfos() {
		final Set<ServletInfo> result = new HashSet<>(super.getServletInfos());
		result.add(new ServletInfo(sampleGuiServlet, SampleGuiConstants.URL_HOME));
		return result;
	}

}
