package de.benjaminborbe.sample.gui;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.osgi.framework.BundleContext;

import com.google.inject.Inject;

import de.benjaminborbe.sample.gui.guice.SampleGuiModules;
import de.benjaminborbe.sample.gui.servlet.SampleGuiServlet;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.HttpBundleActivator;
import de.benjaminborbe.tools.osgi.ServletInfo;

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
		final Set<ServletInfo> result = new HashSet<ServletInfo>(super.getServletInfos());
		result.add(new ServletInfo(sampleGuiServlet, SampleGuiConstants.URL_HOME));
		return result;
	}

}
